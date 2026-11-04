package fr.Valkyar.databaseCommunicationAPI.MendingManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class TraderMendingLIstener implements Listener {
    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        MerchantRecipe recipe = event.getRecipe();
        ItemStack result = recipe.getResult();

        if (result != null && result.getType() == Material.ENCHANTED_BOOK && result.hasItemMeta()) {

            if (result.getItemMeta() instanceof EnchantmentStorageMeta meta) {

                if (meta.hasStoredEnchant(Enchantment.MENDING)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
