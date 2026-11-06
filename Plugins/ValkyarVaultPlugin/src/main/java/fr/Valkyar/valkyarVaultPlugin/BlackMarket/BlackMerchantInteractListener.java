package fr.Valkyar.valkyarVaultPlugin.BlackMarket;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

import static fr.Valkyar.valkyarVaultPlugin.BlackMarket.BlackMerchant.openBlackMarket;

public class BlackMerchantInteractListener implements Listener {

    @EventHandler
    public void onInteractWithMerchant(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof WanderingTrader trader) {

            if (trader.getCustomName() != null && trader.getCustomName().equals(ChatColor.BLACK + "Marché Noir")) {

                event.setCancelled(true);

                openBlackMarket(event.getPlayer());
            }
        }
    }


}
