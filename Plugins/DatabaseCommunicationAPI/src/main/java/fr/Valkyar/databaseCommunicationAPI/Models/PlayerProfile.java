package fr.Valkyar.databaseCommunicationAPI.Models;
import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.Models.JobProgression;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;
import static fr.Valkyar.databaseCommunicationAPI.Utilities.PrestigeTokenUtility.GiveTokenToPlayer;

public class PlayerProfile {
    private UUID playerUuid;
    private Job primaryJob;
    private Job secondaryJob;
    private Double currentMoney;
    private Integer townId;
    private Map<String, Boolean> permissionsMap;
    private Database db;

    public PlayerProfile(UUID playerUuid, Double CurrentMoney, Integer townId, Database db) {
        this.playerUuid = playerUuid;
        currentMoney = CurrentMoney;
        this.townId = townId;
        this.db = db;
    }

    // Integer is the job id
    private final Map<Integer, JobProgression> progressions = new HashMap<>();

    public boolean hasJob(Job job) {
        return (primaryJob.getId().equals(job.getId()))
                || (secondaryJob.getId().equals(job.getId()));
    }
    public boolean hasJob(int jobId) {
        return this.progressions.containsKey(jobId);
    }

    public boolean hasJobInList(List<Job> job) {
        for (Job j : job) {
            if ((primaryJob != null && primaryJob.getId() == j.getId()) || (secondaryJob != null && secondaryJob.getId() == j.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyJobs() {
        return (primaryJob != null || secondaryJob != null);
    }
    public boolean hasMainJobs() {
        return (primaryJob != null);
    }
    public boolean hasSecondJobs() {
        return (secondaryJob != null);
    }
    public boolean hasTown() {
        return townId != null;
    }
    public Boolean hasPermission(String permission) {
        if (!permission.startsWith("town_")) {
            permission = "town_" + permission;
        }
        return permissionsMap.get(permission).booleanValue();
    }
    public Boolean hasHighPermission() {
        return permissionsMap.get("town_mayor") || permissionsMap.get("town_second");
    }
    public Boolean isPrimaryJobHundred() {
        return getPrimaryJobLvl() == 100;
    }
    public Boolean isPrimaryJobMoreThantHundred() {
        return getPrimaryJobLvl() > 100;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
    public Job getPrimaryJob() {
        return primaryJob;
    }
    public Integer getPrimaryJobLvl() {
        return progressions.get(getPrimaryJob().getId()).getLevel();
    }
    public Integer getSecondaryJobLvl() {
        return progressions.get(getSecondaryJob().getId()).getLevel();
    }
    public Job getSecondaryJob() {
        return secondaryJob;
    }
    public Double getCurrentMoney() {return currentMoney;}
    public Integer getTownId() {return townId;}
    public Town getTown() {return TownMap.get(townId);}


    public JobProgression getProgression(Integer jobId) {
        if (progressions.containsKey(jobId)) {
            return progressions.get(jobId);
        }
        return null;
    }

    public void setPrimaryJob(Job primaryJob) {
        this.primaryJob = primaryJob;
    }
    public void setSecondaryJob(Job secondaryJob) {
        this.secondaryJob = secondaryJob;
    }
    public void setTown(Town town) {
        if (town != null) {
            townId = town.getId();
        } else {
            townId = null;
        }
    }
    public void setPermissions(HashMap<String, Boolean> permissions) {
        this.permissionsMap = permissions;
    }
    public void addPermission(String permission) {
        if (!permission.startsWith("town_")) {
            permission = "town_" + permission;
        }
        permissionsMap.put(permission, Boolean.TRUE);
        db.setPermissionOfAPlayerToTrue(Bukkit.getPlayer(playerUuid),permission);
    }
    public void removePermission(String permission) {
        if (!permission.startsWith("town_")) {
            permission = "town_" + permission;
        }
        permissionsMap.put(permission, Boolean.FALSE);
        db.setPermissionOfAPlayerToFalse(Bukkit.getPlayer(playerUuid),permission);
    }

    public void setPermissionsTrue() {
        permissionsMap.replaceAll((key, value) -> true);
        db.setAllPermissionsOfAPlayerToTrue(Bukkit.getPlayer(playerUuid));
    }
    public void ResetPermissions() {
        this.permissionsMap.values().forEach(v -> v = Boolean.FALSE);
        db.setAllPermissionsOfAPlayerToFalse(Bukkit.getPlayer(playerUuid));
    }

    public void increaseExp(Integer jobId, Double amount) {
        if (progressions.get(jobId) == null) {
            return;
        }
        progressions.get(jobId).increaseExperience(amount);

        // TODO : implementer les réel valeurs d'exp nécéssaire pour lvl up
        if (progressions.get(jobId).getExperience() < 100) {
            return;
        }// case player level up
        JobLevelUp(jobId);
        if (isPrimaryJobHundred()) {
            Player p = Bukkit.getPlayer(playerUuid);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
            p.sendMessage(ChatColor.BLUE + "Vous pouvez désormais choisir une spécialisation avec la commande " +
                    ChatColor.BOLD + "/specialisation" + ChatColor.RESET + "" +ChatColor.BLUE + ".");
            p.sendMessage(ChatColor.BLUE + "Vous venez d'atteindre le palier maximum, le post-game commence. Pour commencer voici un Jeton de Prestige.\nVous pouvez accèder à la boutique de prestige avec le /prestige.");
            GiveTokenToPlayer(p);
        } else if (isPrimaryJobMoreThantHundred() && (progressions.get(jobId).getLevel() - 100) % 10 == 0) {
            Player p = Bukkit.getPlayer(playerUuid);
            p.sendMessage(ChatColor.BLUE + "Vous recevez un Jeton de Presitge");
            GiveTokenToPlayer(p);
        }
    }

    public void addProgression(JobProgression progression) {
        this.progressions.put(progression.getJobId(), progression);
    }

    public void increaseMoney(Double amount) {
        currentMoney += amount;
    }
    public void decreaseMoney(Double amount) {
        currentMoney -= amount;
    }
    private void JobLevelUp(int jobId) {
        progressions.get(jobId).setExperience(0);
        progressions.get(jobId).increaseLevel();
        Bukkit.getPlayer(playerUuid).sendMessage("Vous venez de passer au niveau " +
                ChatColor.BOLD + progressions.get(jobId).getLevel() + ChatColor.RESET +
                " dans le métier de " +
                ChatColor.BOLD + progressions.get(jobId).getJob().getDisplayName() + ChatColor.RESET + ".");
    }
}
