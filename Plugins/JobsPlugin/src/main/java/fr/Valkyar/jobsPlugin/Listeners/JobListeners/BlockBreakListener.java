package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.BlockData;
import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import fr.Valkyar.jobsPlugin.PlayerProfile;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreakListener implements Listener {

    private JobsPlugin _plugin;
    private Database db;

    private final Map<String, Job> _materialToJobMap = new HashMap<>();
    private final Map<String, BlockData> _materialToBlockDataMap = new HashMap<>();

    public BlockBreakListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        List<Job> concernedJobs = List.of(
                _plugin.jobsList.get("Mineur"),
                _plugin.jobsList.get("Bucheron"),
                _plugin.jobsList.get("Fermier")
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
    public void onBreakBlock(BlockBreakEvent event) {
        // pour mineur
        // pour bucheron
        // pour fermier (casser plantation)
        //TODO : verifier le block cassé seulement si le block est différent du dernier cassé

        Player p = event.getPlayer();


        PlayerProfile pProfile = _plugin.playerProfiles.get(p.getUniqueId());
        if (pProfile == null || !pProfile.hasAnyJobs()) return;

        Block block = event.getBlock();
        String materialName = event.getBlock().getType().name().toLowerCase();

        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();

            if (ageable.getAge() < ageable.getMaximumAge()) {
                return;
            }
        }

        Job requiredJob = _materialToJobMap.get(materialName);
        if (requiredJob == null) return;

        if (!pProfile.hasJob(requiredJob.getId())) {
            return;
        }

        BlockData blockData = _materialToBlockDataMap.get(materialName);

        if (blockData == null) {
            return;
        }

        pProfile.increaseExp(requiredJob.getId(), blockData.expBringing());



            // TODO : ajouter système d'exp et lvl et argent
    }

}
