package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.ResourceData;
import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.Utilities.PlacedBlockUtilites;
import fr.Valkyar.databaseCommunicationAPI.Utilities.PlacedBlockUtilites.*;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.Valkyar.databaseCommunicationAPI.Utilities.PrestigeTokenUtility.GiveTokenToPlayer;

public class BlockBreakListener implements Listener {

    private JobsPlugin _plugin;
    private Database db;

    private final Map<String, Job> _materialToJobMap = new HashMap<>();
    private final Map<String, ResourceData> _materialToResourceDataMap = new HashMap<>();

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

            List<ResourceData> jobBlocks = db.getResourceByJob(job.getId(), "break");
            for (ResourceData block : jobBlocks) {
                String materialKey = block.getDisplayName().toLowerCase();

                _materialToJobMap.put(materialKey, job);
                _materialToResourceDataMap.put(materialKey, block);
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        // pour mineur
        // pour bucheron
        // pour fermier (casser plantation)

        Player p = event.getPlayer();

        PlayerProfile pProfile = _plugin.databaseApi.playerProfiles.get(p.getUniqueId());
        if (pProfile == null || !pProfile.hasAnyJobs()) return;

        Block block = event.getBlock();

        String materialName = event.getBlock().getType().name().toLowerCase();

        PlacedBlockUtilites.isThisBlockPlaced(block).thenAccept(isPlaced -> {
           if (isPlaced) {
               return;
           }

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

            ResourceData ResourceData = _materialToResourceDataMap.get(materialName);

            if (ResourceData == null) {
                return;
            }

            pProfile.increaseExp(requiredJob.getId(), ResourceData.expBringing());
            pProfile.increaseMoney(ResourceData.moneyBringing());
        });
    }

}
