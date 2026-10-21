package fr.Valkyar.databaseCommunicationAPI.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

public class PrestigeTokenUtility {

    public static boolean isPrestigeToken(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        CustomModelDataComponent modelData = meta.getCustomModelDataComponent();
        List<String> strings = modelData.getStrings();

        return strings != null && strings.contains("prestige_token");
    }

    public static void GiveTokenToPlayer(Player p) {
        ItemStack token = new ItemStack(Material.PAPER);
        ItemMeta meta = token.getItemMeta();

        if (meta != null) {
            CustomModelDataComponent modelData = meta.getCustomModelDataComponent();

            // doit correspondre exactement au nom dans le fichier JSON (prestige_token)
            modelData.setStrings(List.of("prestige_token"));

            meta.setCustomModelDataComponent(modelData);
            meta.setRarity(ItemRarity.EPIC);
            meta.setDisplayName(ChatColor.MAGIC + "Jeton de Prestige");
            token.setItemMeta(meta);
        }

        p.getInventory().addItem(token);
    }
}
