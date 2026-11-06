package fr.Valkyar.valkyarVaultPlugin.BlackMarket;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Merchant;


public class BlackMerchant {

    public static Inventory marketInventory = org.bukkit.Bukkit.createInventory(null, 27, ChatColor.BLACK + "Marché Noir");;

    public BlackMerchant(Location location) {
        WanderingTrader blackMerchant = (WanderingTrader) location.getWorld().spawnEntity(location, EntityType.WANDERING_TRADER);
        blackMerchant.setAI(false);
        blackMerchant.setInvulnerable(true);
        blackMerchant.setCustomName(ChatColor.BLACK + "Marchand Noir");
    }

    public void setBlackMarket() {

    }

    public static void openBlackMarket(Player player) {
        player.sendMessage("debug4");
        player.openInventory(marketInventory);
    }
}
