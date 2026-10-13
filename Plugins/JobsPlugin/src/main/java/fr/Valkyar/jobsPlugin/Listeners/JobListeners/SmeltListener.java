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
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SmeltListener implements Listener {

    private final HashMap<Location, UUID> furnaceCrafters = new HashMap<>();

    private JobsPlugin _plugin;
    private Database db;

    private final Map<String, Job> _materialToJobMap = new HashMap<>();
    private final Map<String, ResourceData> _materialToResourceDataMap = new HashMap<>();

    List<Job> concernedJobs;

    public SmeltListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        concernedJobs = List.of(
                _plugin.jobsList.get("Fondeur")
        );

        for (Job job : concernedJobs) {
            if (job == null) continue;

            List<ResourceData> jobBlocks = db.getResourceByJob(job.getId(), "smelt");
            for (ResourceData block : jobBlocks) {

                String materialKey = block.getDisplayName().toLowerCase();

                _materialToJobMap.put(materialKey, job);
                _materialToResourceDataMap.put(materialKey, block);
            }
        }
    }

    @EventHandler
    // se déclenche quand un joueur clique sur un fourneau ou un haut foureau
        // si l'item mis à cuire est dans les items cuisable alors
        // va stocker dans la HashMap furnaceCrafters l'emplacement du fourneau en clé et le UUID du joueur en valeur
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getType() != InventoryType.FURNACE
                && event.getView().getTopInventory().getType() != InventoryType.BLAST_FURNACE) {
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
        };

        FurnaceInventory inv = (FurnaceInventory) event.getView().getTopInventory();
        Block furnaceBlock = inv.getLocation().getBlock();

        if (event.getRawSlot() == 0) {
            furnaceCrafters.put(furnaceBlock.getLocation(), p.getUniqueId());
        }
    }

    private boolean isCookable(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        return _materialToResourceDataMap.get(item.getType().name().toLowerCase()) != null;
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        // pour fondeur
        // récupère le UUID du joueur par rapport à l'emplacement du fourneau qui viens de finir de cuire
        UUID playerId = furnaceCrafters.get(event.getBlock().getLocation());

        if (playerId == null) {
            return;
        }

        Player p = Bukkit.getPlayer(playerId);

        if (p == null || !p.isOnline()) {
            return;
        }

        ItemStack result = event.getResult();

        if (!isCookable(result)) {
            return;
        }

        Job requiredJob = _materialToJobMap.get(result.getType().name().toLowerCase());
        if (requiredJob == null) return;

        ResourceData ResourceData = _materialToResourceDataMap.get(result.getType().name().toLowerCase());

        if (ResourceData == null) {
            return;
        }
        PlayerProfile pProfile = _plugin.databaseApi.playerProfiles.get(p.getUniqueId());

        pProfile.increaseExp(requiredJob.getId(), ResourceData.expBringing());
        pProfile.increaseMoney(ResourceData.moneyBringing());
    }
}
