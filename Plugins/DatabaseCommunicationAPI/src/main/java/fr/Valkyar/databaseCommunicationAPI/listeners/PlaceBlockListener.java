package fr.Valkyar.databaseCommunicationAPI.listeners;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.BlockData;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.PlacedBlocks;

public class PlaceBlockListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        DatabaseCommunicationAPI.BlockPosition blockPosition = new DatabaseCommunicationAPI.BlockPosition(
                block.getWorld().getName(),
                block.getX(), block.getY(), block.getZ()
        );

        BlockData blockData = new BlockData(
                event.getPlayer().getUniqueId(),
                block.getWorld().getName(),
                block.getX(), block.getY(), block.getZ(),
                block.getType().toString()
        );

        PlacedBlocks.put(blockPosition, blockData);
    }
}
