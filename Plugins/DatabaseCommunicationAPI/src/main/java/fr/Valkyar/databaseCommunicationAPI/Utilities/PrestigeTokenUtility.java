package fr.Valkyar.databaseCommunicationAPI.Utilities;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.*;

import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.PrestigeToken;

public class PrestigeTokenUtility {

    public static ItemStack DefinPresigeToken() {
        ItemStack token = new ItemStack(Material.PAPER);
        ItemMeta meta = token.getItemMeta();

        if (meta != null) {
            CustomModelDataComponent modelData = meta.getCustomModelDataComponent();

            // doit correspondre exactement au nom dans le fichier JSON (prestige_token)
            modelData.setStrings(List.of("prestige_token"));

            meta.setCustomModelDataComponent(modelData);
            meta.setRarity(ItemRarity.EPIC);
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Jeton de Prestige");
            meta.setItemName("Jeton de Prestige");
            token.setItemMeta(meta);
        }
        return token;
    }

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
        p.getInventory().addItem(PrestigeToken);
    }

    public static int CountPrestigeTokens(Player p) {
        int playerPrestigeToken = 0;

        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null || item.getType() != Material.PAPER || item.getItemMeta() == null) {
                continue;
            }
            if (item.getItemMeta().getItemName().equalsIgnoreCase("Jeton de Prestige")) {
                playerPrestigeToken += item.getAmount();
            }
        }
        return playerPrestigeToken;
    }
}
