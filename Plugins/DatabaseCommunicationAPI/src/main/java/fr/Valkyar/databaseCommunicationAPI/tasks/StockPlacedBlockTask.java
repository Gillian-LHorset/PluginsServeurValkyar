package fr.Valkyar.databaseCommunicationAPI.tasks;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.BlockData;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;
import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.PlacedBlocks;

public class StockPlacedBlockTask extends BukkitRunnable {

    private DatabaseCommunicationAPI _plugin;

    public StockPlacedBlockTask(DatabaseCommunicationAPI plugin) {
        _plugin = plugin;
    }

    @Override
    public void run() {
        if (PlacedBlocks.isEmpty()) {
            return;
        }
        // copy the PlacedBlocks HashMap
        List<BlockData> blocksToSave = new ArrayList<>(PlacedBlocks.values());
        // clear the real list of placed block in memory
        PlacedBlocks.clear();
        // save the placed blocks in db from the copy
        _plugin.databaseLink.insertBlockBatch(blocksToSave);
    }
}
