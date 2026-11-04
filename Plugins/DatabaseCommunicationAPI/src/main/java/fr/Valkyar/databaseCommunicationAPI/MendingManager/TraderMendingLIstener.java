package fr.Valkyar.databaseCommunicationAPI.MendingManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.PlayerFishEvent;
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

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }
        Entity caught = event.getCaught();

        if (caught instanceof Item itemEntity) {
            ItemStack item = itemEntity.getItemStack();

            if (item.getType() != Material.ENCHANTED_BOOK || item.getItemMeta() instanceof EnchantmentStorageMeta) {
                return;
            }

            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

            if (!meta.hasStoredEnchant(Enchantment.MENDING)) {
                return;
            }

            double dropChance = 0.20; // 20%

            if (Math.random() < dropChance) {
                return;
            }
            event.setCancelled(true);
        }

    }

}