package fr.Valkyar.databaseCommunicationAPI.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import static fr.Valkyar.databaseCommunicationAPI.Utilities.PrestigeTokenUtility.isPrestigeToken;

public class ItemProtectionListener implements Listener {
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (isPrestigeToken(item)) {
                // annule l'affichage du résultat dans la table de craft
                event.getInventory().setResult(null);
                break;
            }
        }
    }
}
