package fr.Valkyar.jobsPlugin.Commands.prestigeCommands;

import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrestigeShop {

    public static List<Map<?, ?>> itemsList;
    public static NamespacedKey prestigeItemCostKey;
    private Inventory _shop;
    public PrestigeShop(JobsPlugin plugin, FileConfiguration config) {
        itemsList = config.getMapList("boutique.items");
        _shop = Bukkit.createInventory(null ,54, ChatColor.DARK_PURPLE + config.getString("boutique.name"));

        for (Map<?, ?> itemMap : itemsList) {
            String materialName = (String) itemMap.get("material");
            Material material = Material.matchMaterial(materialName);

            if (material == null) {
                continue;
            }
            ItemStack item = new ItemStack(material);

            List<Map<?, ?>> enchantments = (List<Map<?, ?>>) itemMap.get("enchantments");
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_PURPLE + "" + itemMap.get("name"));

            // add the coast of the item in the PersistentDataContainer
            int prestigeItemCost = (int) itemMap.get("cost");
            prestigeItemCostKey = new NamespacedKey(plugin, "prestige_cost");

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(prestigeItemCostKey, PersistentDataType.INTEGER, prestigeItemCost);

            if (meta == null) {
                continue;
            }

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
            item.setItemMeta(meta);


            _shop.addItem(item);
        }
    }

    public void openPrestigeShop(Player p) {
        p.openInventory(_shop);
    }
}
