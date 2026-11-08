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
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin.config;


public class BlackMerchant {
    private final ValkyarVaultPlugin _plugin;

    public static final String MENU_NAME = ChatColor.BLACK + "Marché Noir";
    public static final String TRADER_NAME = ChatColor.BLACK + "Marchand Noir";
    // the protection of the inventory is in DatabaseCommunicationAPI
    public static Inventory marketInventory = org.bukkit.Bukkit.createInventory(null, 27, MENU_NAME);;

    public static NamespacedKey itemCostKey;
    public static NamespacedKey enchantKey;
    private HashMap<String, ItemStack> allItemsMap = new HashMap<>();

    public BlackMerchant(ValkyarVaultPlugin plugin) {
        _plugin = plugin;
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
        }.runTaskLater(_plugin, 20L * 5);
    }

    public static record ItemPrice_MoneyType(int price, String moneyType){}

    private void setBlackMarket() {
        List<Map<?, ?>> itemsList = config.getMapList("BlackMarketItems");

        for (Map<?, ?> itemMap : itemsList) {
            Material material = Material.matchMaterial((String) itemMap.get("material"));

            ItemStack item = new ItemStack(material);

            ItemMeta meta = item.getItemMeta();

            // about the price
            int ItemPrice = (int) itemMap.get("price");
            String ItemMoneyType = (String) itemMap.get("money");

            ItemPrice_MoneyType data = new ItemPrice_MoneyType(ItemPrice, ItemMoneyType);

            Gson gson = new Gson();
            String jsonString = gson.toJson(data);

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            // add the price of the item in the PersistentDataContainer
            itemCostKey = new NamespacedKey(_plugin, "item_price");

            dataContainer.set(itemCostKey, PersistentDataType.STRING, jsonString);
            ///////////////////

            List<Map<?, ?>> enchantments = (List<Map<?, ?>>) itemMap.get("enchantments");
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
            item.setItemMeta(meta);

            String rarity = (String) itemMap.get("rarity");
            allItemsMap.put(rarity, item);
        }

    }

    public static void openBlackMarket(Player player) {
        player.openInventory(marketInventory);
    }
}
