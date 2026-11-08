package fr.Valkyar.valkyarVaultPlugin.BlackMarket;

import com.google.gson.Gson;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.PrestigeToken;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;
import static fr.Valkyar.databaseCommunicationAPI.Utilities.PrestigeTokenUtility.CountPrestigeTokens;
import static fr.Valkyar.valkyarVaultPlugin.BlackMarket.BlackMerchant.*;

public class BlackMerchantInteractListener implements Listener {


    @EventHandler
    public void onInteractWithMerchant(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof WanderingTrader trader) {

            if (trader.getCustomName() != null && trader.getCustomName().equalsIgnoreCase(TRADER_NAME)) {

                event.setCancelled(true);

                openBlackMarket(event.getPlayer());
            }
        }
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
            p.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent ou de Jeton de Prestige pour acheter ceci.");
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

    private boolean CanPlayerBuyItem(Player p, ItemStack item) {
        PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

        if (itemData == null) {
            return false;
        }

        if (!itemData.has(itemCostKey, PersistentDataType.STRING)) {
            return false;
        }
        String jsonString = itemData.get(itemCostKey, PersistentDataType.STRING);

        if (jsonString == null) {
            return false;
        }

        Gson gson = new Gson();
        ItemPrice_MoneyType data = gson.fromJson(jsonString, ItemPrice_MoneyType.class);

        int price = data.price();
        String moneyType = data.moneyType();

        if (moneyType.equalsIgnoreCase("pièce")) {
            PlayerProfile pP = playerProfiles.get(p.getUniqueId());
            return pP.getCurrentMoney() >= price;
        }

        return CountPrestigeTokens(p) >= price;
    }

    private void BuyItem(Player p, ItemStack item) {
        String jsonString = item.getItemMeta().getPersistentDataContainer().get(itemCostKey, PersistentDataType.STRING);

        Gson gson = new Gson();
        ItemPrice_MoneyType data = gson.fromJson(jsonString, ItemPrice_MoneyType.class);

        int price = data.price();

        for (int i = 0; i < price; i++) {
            p.getInventory().removeItem(PrestigeToken);
        }
        p.sendMessage(ChatColor.GREEN + "Vous avez acheter " + (item.getItemMeta().getDisplayName().isEmpty() ? item.getType().toString().toLowerCase() : item.getItemMeta().getDisplayName().toLowerCase()) + ".");

        Map<Integer, ItemStack> leftOver = p.getInventory().addItem(resetItemLore(item.clone()));

        if (!leftOver.isEmpty()) {
            Location loc = p.getLocation();

            for (ItemStack itemDrop : leftOver.values()) {
                loc.getWorld().dropItemNaturally(loc, itemDrop);
            }

            p.sendMessage(ChatColor.RED + "Votre inventaire est plein, l'objet est tombé au sol !");
        }
    }

    private ItemStack resetItemLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        List<String> newLore = meta.getLore();
        newLore.removeLast();
        meta.setLore(newLore);
        item.setItemMeta(meta);

        return item;
    }


}
