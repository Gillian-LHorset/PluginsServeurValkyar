package fr.Valkyar.jobsPlugin.Commands.prestigeCommands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class PrestigeShop {

    public static List<Map<?, ?>> itemsList;
    public static NamespacedKey prestigeItemCostKey;
    public static NamespacedKey prestigeItemJobKey;
    public static NamespacedKey prestigeItemSpecialisationKey;
    private Inventory _shop;

    public PrestigeShop(JobsPlugin plugin, FileConfiguration config) {
        itemsList = config.getMapList("boutique.items");
        _shop = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + config.getString("boutique.name"));

        for (Map<?, ?> itemMap : itemsList) {
            String materialName = (String) itemMap.get("material");
            Material material = Material.matchMaterial(materialName);

            if (material == null) {
                continue;
            }
            ItemStack item = new ItemStack(material);

            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                continue;
            }
            meta.setDisplayName(ChatColor.DARK_PURPLE + "" + itemMap.get("name"));

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

            // add the cost of the item in the PersistentDataContainer
            int prestigeItemCost = (int) itemMap.get("cost");
            prestigeItemCostKey = new NamespacedKey(plugin, "prestige_cost");

            dataContainer.set(prestigeItemCostKey, PersistentDataType.INTEGER, prestigeItemCost);
            ///////////////////

            // add the job of the item in the PersistentDataContainer
            String prestigeItemJob = (String) itemMap.get("metier");
            prestigeItemJobKey = new NamespacedKey(plugin, "prestige_job");

            dataContainer.set(prestigeItemJobKey, PersistentDataType.STRING, prestigeItemJob);
            ///////////////////

            // add the specialisation of the item in the PersistentDataContainer
            String prestigeItemSpecialisation = (String) itemMap.get("specialisation");
            prestigeItemSpecialisationKey = new NamespacedKey(plugin, "prestige_specialisation");

            dataContainer.set(prestigeItemSpecialisationKey, PersistentDataType.STRING, prestigeItemSpecialisation);
            ///////////////////


            List<Map<?, ?>> enchantments = (List<Map<?, ?>>) itemMap.get("enchantments");
            for (Map<?, ?> enchMap : enchantments) {
                String enchName = (String) enchMap.get("name");
                int level = (int) enchMap.get("level");

                NamespacedKey enchantKey = NamespacedKey.minecraft(enchName.toLowerCase());
                Enchantment enchant = Registry.ENCHANTMENT.get(enchantKey);

                if (enchant == null) {
                    continue;
                }

                // le 'true' permet d'appliquer des niveaux supérieurs aux limites vanilla
                meta.addEnchant(enchant, level, true);
            }

            // set lore
            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.DARK_PURPLE + "Métier : " + ChatColor.YELLOW + prestigeItemJob);
            lore.add(ChatColor.DARK_PURPLE + "Spécialisation : " + ChatColor.YELLOW + prestigeItemSpecialisation);
            lore.add(ChatColor.DARK_PURPLE + "Prix : " + ChatColor.YELLOW + prestigeItemCost + " Jetons de Prestige");

            meta.setLore(lore);

            item.setItemMeta(meta);


            _shop.addItem(item);
        }
    }

    public void openPrestigeShop(Player p) {
        PlayerProfile pP = playerProfiles.get(p.getUniqueId());
        ItemStack[] filtered = Arrays.stream(_shop.getContents())
                .filter(i -> i != null && pP.getSpecialisation().equalsIgnoreCase(getPrestigeItemSpecialisation(i)))
                .toArray(ItemStack[]::new);

        _shop.clear();
        _shop.setContents(filtered);


        p.openInventory(_shop);
    }

    private String getPrestigeItemSpecialisation(ItemStack prestigeItem) {
        PersistentDataContainer itemData = prestigeItem.getItemMeta().getPersistentDataContainer();

        if (itemData == null) {
            return null;
        }

        if (!itemData.has(prestigeItemSpecialisationKey, PersistentDataType.STRING)) {
            return null;
        }
        String spe = itemData.get(prestigeItemSpecialisationKey, PersistentDataType.STRING);

        if (spe == null) {
            return null;
        }

        return spe;
    }

}
