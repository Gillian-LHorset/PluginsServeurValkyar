package fr.Valkyar.jobsPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.PrestigeToken;
import static fr.Valkyar.databaseCommunicationAPI.Utilities.PrestigeTokenUtility.CountPrestigeTokens;
import static fr.Valkyar.jobsPlugin.Commands.prestigeCommands.PrestigeShop.prestigeItemCostKey;

public class PrestigeShopMenuHandler implements Listener {

    private final JobsPlugin _plugin;
    public final String MENU_NAME;

    public PrestigeShopMenuHandler(JobsPlugin plugin) {
        _plugin = plugin;
        MENU_NAME = ChatColor.DARK_PURPLE + _plugin.config.getString("boutique.name");
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null || !e.getView().getTitle().equalsIgnoreCase(MENU_NAME)) {
            return;
        }

        if (e.getClickedInventory().equals(e.getView().getTopInventory())) {
            e.setCancelled(true);
        }

        if (e.getCurrentItem() == null
                || e.getCurrentItem().getType().isAir()
                ) {
            return;
        }

        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            e.setCancelled(true);
            return;
        }

        // if player click on his own inventory
        if (e.getClickedInventory().equals(p.getInventory())) {
            return;
        }

        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();

        if (!CanPlayerBuyItem(p, item)) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas assez de Jeton de Prestige pour acheter ceci.");
            return;
        }
        BuyItem(p, item);
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase(MENU_NAME)) {
            return;
        }

        // si le joueur essaie de glisser/répartir des items dans l'inventaire du haut
        for (int rawSlot : e.getRawSlots()) {
            if (rawSlot < e.getView().getTopInventory().getSize()) {
                e.setCancelled(true);
                return;
            }
        }
    }

    public boolean CanPlayerBuyItem(Player p, ItemStack prestigeItem) {
        PersistentDataContainer itemData = prestigeItem.getItemMeta().getPersistentDataContainer();

        if (itemData == null) {
            return false;
        }

        if (!itemData.has(prestigeItemCostKey, PersistentDataType.INTEGER)) {
            return false;
        }
        Integer itemCost = itemData.get(prestigeItemCostKey, PersistentDataType.INTEGER);

        if (itemCost == null) {
            return false;
        }

        return CountPrestigeTokens(p) >= itemCost;
    }

    public void BuyItem(Player p, ItemStack prestigeItem) {
        Integer itemCost = prestigeItem.getItemMeta().getPersistentDataContainer().get(prestigeItemCostKey, PersistentDataType.INTEGER);
        for (int i = 0; i < itemCost; i++) {
            p.getInventory().removeItem(PrestigeToken);
        }
        p.sendMessage(ChatColor.GREEN + "Félicitation, vous avez acheter " + prestigeItem.getItemMeta().getDisplayName() + ".");
        p.getInventory().addItem(prestigeItem);
    }
}
