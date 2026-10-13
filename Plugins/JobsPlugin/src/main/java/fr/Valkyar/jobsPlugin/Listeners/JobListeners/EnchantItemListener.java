package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.Models.ResourceData;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantItemListener implements Listener {

    private JobsPlugin _plugin;
    private Database db;

    private final Map<String, Job> _materialToJobMap = new HashMap<>();
    private final Map<String, ResourceData> _materialToResourceDataMap = new HashMap<>();

    List<Job> concernedJobs;

    public EnchantItemListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        concernedJobs = List.of(
                _plugin.jobsList.get("Forgemage")
        );

        for (Job job : concernedJobs) {
            if (job == null) continue;

            List<ResourceData> jobBlocks = db.getResourceByJob(job.getId(), "enchant");
            for (ResourceData block : jobBlocks) {

                String materialKey = block.getDisplayName().toLowerCase();

                _materialToJobMap.put(materialKey, job);
                _materialToResourceDataMap.put(materialKey, block);
            }
        }
    }


    @EventHandler
    public void onItemEnchant(EnchantItemEvent event) {
        // pour forgemage (enchant item)

        Player p = event.getEnchanter();

        PlayerProfile pProfile = _plugin.databaseApi.playerProfiles.get(p.getUniqueId());
        if (pProfile == null || !pProfile.hasAnyJobs()) return;

        String materialName = event.getItem().getType().name().toLowerCase();


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
    }
}
