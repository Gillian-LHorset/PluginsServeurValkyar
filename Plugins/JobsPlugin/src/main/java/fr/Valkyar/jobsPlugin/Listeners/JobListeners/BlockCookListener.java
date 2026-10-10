package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.BlockData;
import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockCookListener implements Listener {


    private JobsPlugin _plugin;
    private Database db;

    private final Map<String, Job> _materialToJobMap = new HashMap<>();
    private final Map<String, BlockData> _materialToBlockDataMap = new HashMap<>();

    public BlockCookListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        List<Job> concernedJobs = List.of(
                _plugin.jobsList.get("Cuisinier")
        );

        for (Job job : concernedJobs) {
            if (job == null) continue;

            List<BlockData> jobBlocks = db.getBreakableBlocksByJob(job.getId());
            for (BlockData block : jobBlocks) {
                String materialKey = block.getDisplayName().toLowerCase();

                _materialToJobMap.put(materialKey, job);
                _materialToBlockDataMap.put(materialKey, block);
            }
        }
    }


    @EventHandler
    public void onFoodCook(BlockCookEvent e) {
        // pour cuisinier
    }
}
