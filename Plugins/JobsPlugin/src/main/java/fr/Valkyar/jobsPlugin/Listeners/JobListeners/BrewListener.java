package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.Models.ResourceData;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BrewListener implements Listener {

    private final HashMap<Location, UUID> brewingCrafters = new HashMap<>();

    private JobsPlugin _plugin;
    private Database db;

    private final Map<String, Job> _materialToJobMap = new HashMap<>();
    private final Map<String, ResourceData> _materialToResourceDataMap = new HashMap<>();

    List<Job> concernedJobs;

    public BrewListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        concernedJobs = List.of(
                _plugin.jobsList.get("Alchimiste")
        );

        for (Job job : concernedJobs) {
            if (job == null) continue;
            List<ResourceData> jobItems = db.getResourceByJob(job.getId(), "brew");
            for (ResourceData item : jobItems) {

                String materialKey = item.getDisplayName().toLowerCase();

                _materialToJobMap.put(materialKey, job);
                _materialToResourceDataMap.put(materialKey, item);
            }
        }
    }


    @EventHandler
    // Se déclenche quand un joueur clique sur un alambic (Brewing Stand)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getType() != InventoryType.BREWING) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) event.getWhoClicked();

        PlayerProfile pProfile = _plugin.databaseApi.playerProfiles.get(p.getUniqueId());
        if (pProfile == null || !pProfile.hasAnyJobs()) return;

        if (!pProfile.hasJobInList(concernedJobs)) {
            return;
        }

        BrewerInventory inv = (BrewerInventory) event.getView().getTopInventory();

        if (inv.getLocation() == null || inv.getLocation().getBlock() == null) {
            return;
        }

        Block brewingBlock = inv.getLocation().getBlock();

        int slot = event.getRawSlot();
        if (slot >= 0 && slot <= 4) {
            brewingCrafters.put(brewingBlock.getLocation(), p.getUniqueId());
        }
    }

    private boolean isBrewable(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        String potionKey = getPotionKey(item);
        if (potionKey == null) {
            return false;
        }

        return _materialToResourceDataMap.containsKey(potionKey);
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        // Récupère le UUID du joueur par rapport à l'emplacement de l'alambic
        UUID playerId = brewingCrafters.get(event.getBlock().getLocation());

        if (playerId == null) {
            return;
        }

        Player p = Bukkit.getPlayer(playerId);

        if (p == null || !p.isOnline()) {
            return;
        }

        // On utilise getResults() pour attraper les potions fabriquées et non l'eau de départ
        List<ItemStack> results = event.getResults();

        for (ItemStack item : results) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            String potionKey = getPotionKey(item);

            if (potionKey == null || !_materialToResourceDataMap.containsKey(potionKey)) {
                continue;
            }


            Job requiredJob = _materialToJobMap.get(potionKey);
            ResourceData resourceData = _materialToResourceDataMap.get(potionKey);

            if (requiredJob == null || resourceData == null) continue;

            PlayerProfile pProfile = _plugin.databaseApi.playerProfiles.get(p.getUniqueId());
            if (pProfile == null) continue;

            pProfile.increaseExp(requiredJob.getId(), resourceData.expBringing());
            pProfile.increaseMoney(resourceData.moneyBringing());
        }
    }

    private String getPotionKey(ItemStack item) {
        if (item.getItemMeta() instanceof org.bukkit.inventory.meta.PotionMeta meta) {
            org.bukkit.potion.PotionType potionType = meta.getBasePotionType();
            if (potionType != null) {
                return potionType.name().toLowerCase();
            }
        }
        return null;
    }
}
