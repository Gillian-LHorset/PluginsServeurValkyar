package fr.Valkyar.databaseCommunicationAPI.Utilities;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.block.Block;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.PlacedBlocks;

public class PlacedBlockUtilites {

    public static boolean isThisBlockPlaced(Block block) {
        if (block == null) {
            return false;
        }

        DatabaseCommunicationAPI.BlockPosition blockPosition = new DatabaseCommunicationAPI.BlockPosition(
                block.getWorld().getName(), block.getX(), block.getY(), block.getZ()
        );


        return PlacedBlocks.get(blockPosition) != null
                || DatabaseCommunicationAPI.getApi().databaseLink.getPlacedBlockByLocation(blockPosition) != null;
    }
}
