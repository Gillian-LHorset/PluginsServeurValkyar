package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.ResourceData;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import fr.Valkyar.jobsPlugin.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnvilListener implements Listener {

    private JobsPlugin _plugin;
    private Database db;

    private final Map<String, Job> _actionToJobMap = new HashMap<>();
    private final Map<String, ResourceData> _actionToResourceDataMap = new HashMap<>();

    List<Job> concernedJobs;

    public AnvilListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        concernedJobs = List.of(
                _plugin.jobsList.get("Forgemage")
        );

        List<String> actions = List.of("repaire", "enchant");

        for (Job job : concernedJobs) {
            if (job == null) continue;

            for (String action : actions) {
                List<ResourceData> resources = db.getResourceByJob(job.getId(), action);

                for (ResourceData resource : resources) {
                    String materialKey = resource.getDisplayName().toLowerCase();

                    // Clé unique, par exemple : "repaire:diamond_sword" ou "enchant:diamond_sword"
                    String compositeKey = action + ":" + materialKey;

                    _actionToJobMap.put(compositeKey, job);
                    _actionToResourceDataMap.put(compositeKey, resource);
                }
            }
        }
    }

    @EventHandler
    public void onPrepareAnvil(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getType() != InventoryType.ANVIL) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) return;

        if (event.getRawSlot() != 2) {
            return;
        }

        Player p = (Player) event.getWhoClicked();

        PlayerProfile pProfile = _plugin.playerProfiles.get(p.getUniqueId());
        if (pProfile == null || !pProfile.hasJobInList(concernedJobs)) {
            return;
        }

        ItemStack resultItem = event.getCurrentItem();

        if (resultItem == null || resultItem.getType() == Material.AIR) {
            return;
        }

        AnvilInventory inv = (AnvilInventory) event.getView().getTopInventory();
        ItemStack secondItem = inv.getItem(1);
        String materialKey = resultItem.getType().name().toLowerCase();

        // détermine s'il s'agit d'un enchantement ou d'une réparation
        String actionType = (secondItem != null && secondItem.getType() == Material.ENCHANTED_BOOK)
                ? "enchant"
                : "repaire";

        String compositeKey = actionType + ":" + materialKey;

        Job targetJob = _actionToJobMap.get(compositeKey);
        ResourceData targetData = _actionToResourceDataMap.get(compositeKey);

        if (targetJob != null && targetData != null) {
            pProfile.increaseExp(targetJob.getId(), targetData.expBringing());
            pProfile.increaseMoney(targetData.moneyBringing());
        }
    }
}
