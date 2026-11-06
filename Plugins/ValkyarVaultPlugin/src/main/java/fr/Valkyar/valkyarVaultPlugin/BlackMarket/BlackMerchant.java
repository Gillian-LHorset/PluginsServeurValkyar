package fr.Valkyar.valkyarVaultPlugin.BlackMarket;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.Merchant;


public class BlackMerchant {
    public BlackMerchant(Location location) {
        WanderingTrader blackMerchant = (WanderingTrader) location.getWorld().spawnEntity(location, EntityType.WANDERING_TRADER);
        blackMerchant.setAI(false);
    }
}
