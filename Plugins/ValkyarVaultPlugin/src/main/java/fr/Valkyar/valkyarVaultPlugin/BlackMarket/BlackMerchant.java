package fr.Valkyar.valkyarVaultPlugin.BlackMarket;

import com.google.gson.Gson;
import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin.config;


public class BlackMerchant {
    private final ValkyarVaultPlugin _plugin;

    public static final String MENU_NAME = ChatColor.BLACK + "Marché Noir";
    public static final String TRADER_NAME = ChatColor.BLACK + "Marchand Noir";
    // the protection of the inventory is in DatabaseCommunicationAPI
    public static Inventory marketInventory = org.bukkit.Bukkit.createInventory(null, 27, MENU_NAME);;

    private Map<String, Integer> rarityAmountMap = new HashMap<>();

    public static NamespacedKey itemCostKey;
    public static NamespacedKey enchantKey;
    private HashMap<String, List<ItemStack>> allItemsMap = new HashMap<>() {};

    public BlackMerchant(ValkyarVaultPlugin plugin) {
        _plugin = plugin;

        List<Map<?, ?>> tempMap = config.getMapList("BlackMarketItemsRarity");
        tempMap.forEach(map -> {
            map.keySet()
        })
        //rarityAmountMap
    }

    public void CreateBlackMerchant(Location loc) {
        WanderingTrader blackMerchant = (WanderingTrader) loc.getWorld().spawnEntity(loc, EntityType.WANDERING_TRADER);
        blackMerchant.setAI(false);
        blackMerchant.setInvulnerable(true);
        blackMerchant.setCustomName(TRADER_NAME);
        setBlackMarket();
        new BukkitRunnable() {
            @Override
            public void run() {
                blackMerchant.remove();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    String title = player.getOpenInventory().getTitle();
                    if (title != null && title.equalsIgnoreCase(MENU_NAME)) {
                        player.closeInventory();
                    }
                }
                marketInventory.clear();
            }
            // 10m
        }.runTaskLater(_plugin, 20L * 60 * 10);
    }

    public static record ItemPrice_MoneyType(int price, String moneyType){}

    private void setBlackMarket() {
        List<Map<?, ?>> itemsConfig = config.getMapList("BlackMarketItems");

        for (Map<?, ?> itemConfig : itemsConfig) {
            Material material = Material.matchMaterial((String) itemConfig.get("material"));

            ItemStack item = new ItemStack(material);

            ItemMeta meta = item.getItemMeta();

            // about the price
            int ItemPrice = (int) itemConfig.get("price");
            String ItemMoneyType = (String) itemConfig.get("money");

            ItemPrice_MoneyType data = new ItemPrice_MoneyType(ItemPrice, ItemMoneyType);

            Gson gson = new Gson();
            String jsonString = gson.toJson(data);

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            // add the price of the item in the PersistentDataContainer
            itemCostKey = new NamespacedKey(_plugin, "item_price");

            dataContainer.set(itemCostKey, PersistentDataType.STRING, jsonString);
            ///////////////////

            List<Map<?, ?>> enchantments = (List<Map<?, ?>>) itemConfig.get("enchantments");
            if (enchantments != null) {
                for (Map<?, ?> enchMap : enchantments) {
                    String enchName = (String) enchMap.get("name");
                    int level = (int) enchMap.get("level");

                    enchantKey = NamespacedKey.minecraft(enchName.toLowerCase());
                    Enchantment enchant = Registry.ENCHANTMENT.get(enchantKey);

                    if (enchant == null) {
                        continue;
                    }

                    // vérifie si l'item est un livre enchanté
                    if (meta instanceof EnchantmentStorageMeta storageMeta) {
                        // le 'true' permet d'appliquer des niveaux supérieurs aux limites vanilla
                        storageMeta.addStoredEnchant(enchant, level, true);
                    } else {
                        // le 'true' permet d'appliquer des niveaux supérieurs aux limites vanilla
                        meta.addEnchant(enchant, level, true);
                    }
                }
            }

            // set lore
            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.DARK_PURPLE + "Prix : " + ChatColor.YELLOW + ItemPrice + " " + ItemMoneyType);

            meta.setLore(lore);
            // set meta
            item.setItemMeta(meta);

            String rarity = (String) itemConfig.get("rarity");
            allItemsMap.get(rarity).add(item);
        }
    }

    public List<ItemStack> getRandomShopItems(Map<String, List<ItemStack>> allItemsMap, Map<String, Integer> amountsToPick) {
        List<ItemStack> selectedItems = new ArrayList<>();

        // On parcourt chaque rareté et le nombre d'items souhaités
        for (Map.Entry<String, Integer> entry : amountsToPick.entrySet()) {
            String rarity = entry.getKey();
            int amount = entry.getValue();

            List<ItemStack> availableItems = allItemsMap.get(rarity);

            // Vérifie que la liste existe et n'est pas vide pour éviter les erreurs
            if (availableItems != null && !availableItems.isEmpty()) {
                for (int i = 0; i < amount; i++) {
                    // Choisit un index aléatoire (les doublons sont autorisés naturellement ici)
                    int randomIndex = random.nextInt(availableItems.size());
                    selectedItems.add(availableItems.get(randomIndex));
                }
            }
        }

        return selectedItems;
    }

    public static void openBlackMarket(Player player) {
        player.openInventory(marketInventory);
    }
}
