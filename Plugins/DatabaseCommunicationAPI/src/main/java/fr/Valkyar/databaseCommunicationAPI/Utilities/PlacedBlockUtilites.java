package fr.Valkyar.databaseCommunicationAPI.Utilities;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import org.bukkit.block.Block;

import java.util.concurrent.CompletableFuture;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.PlacedBlocks;

public class PlacedBlockUtilites {

    public static CompletableFuture<Boolean> isThisBlockPlaced(Block block) {
        if (block == null) {
            return CompletableFuture.completedFuture(false);
        }

        DatabaseCommunicationAPI.BlockPosition blockPosition = new DatabaseCommunicationAPI.BlockPosition(
                block.getWorld().getName(), block.getX(), block.getY(), block.getZ()
        );

        if (DatabaseCommunicationAPI.getPlacedBlocks().containsKey(blockPosition)) {
            return CompletableFuture.completedFuture(true);
        }

        return DatabaseCommunicationAPI.getApi().databaseLink.getPlacedBlockByLocation(blockPosition)
                .thenApply(blockData -> blockData != null);
    }
}
