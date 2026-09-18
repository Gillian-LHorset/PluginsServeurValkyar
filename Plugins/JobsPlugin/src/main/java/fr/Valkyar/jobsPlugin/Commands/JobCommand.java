package fr.Valkyar.jobsPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.JobProgression;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.PlayerProfile;
import fr.Valkyar.jobsPlugin.Utility.PlayerProfileUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.Valkyar.jobsPlugin.JobsPlugin;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static fr.Valkyar.jobsPlugin.Utility.PlayerProfileUtility.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class JobCommand implements CommandExecutor {
    private JobsPlugin _plugin;

    private Database db;

    public JobCommand(JobsPlugin plugin) {
        _plugin = plugin;

        db = plugin.databaseApi.databaseLink;
    }

    private final int[] _jobLvlCheckpoint = new int[]{10, 20, 40, 75, 100};
    private final Duration _mainJobCooldown = Duration.ofDays(6);
    private final Duration _secondJobCooldown = Duration.ofHours(5);

    List<String> jobs;
    int index = 1;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {

            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.RED + "Utilisation de la commande :\n/<command> <argument> <argument>");
                return true;
            }

            PlayerProfile pProfile = _plugin.playerProfiles.get(player.getUniqueId());

            switch (args[0]) {
                case "join":

                    if (args.length == 1) {
                        player.sendMessage(ChatColor.RED + "Veuillez sélectionner un type métier à rejoindre.");
                        return true;
                    }

                    if (args.length == 2) {
                        player.sendMessage(ChatColor.RED + "Veuillez sélectionner un métier à rejoindre.");
                        return true;
                    }

                    if (args.length != 3) {
                        player.sendMessage(ChatColor.RED + "Erreur dans la commande.");
                        return true;
                    }

                    Job job = _plugin.jobsList.get(args[2]);

                    if (job == null) {
                        player.sendMessage(ChatColor.RED + "Le métier ciblé n'existe pas.");
                        return true;
                    }

                    if (job.isPrimary()) {
                        // join main job logic
                        if (pProfile.hasMainJobs()) {
                            player.sendMessage(ChatColor.RED + "Vous avez déjà un métier principal.");
                            return true;
                        }

                        if (!IsCoolDownEnded(player, job, _mainJobCooldown)) {
                            return true;
                        }

                        RegisterJob(player, job);
                    } else {
                        // secondary job logic
                        if (!pProfile.hasSecondJobs()) {
                            if (!IsCoolDownEnded(player, job, _secondJobCooldown)) {
                                return true;
                            }

                            if (db.getJobProgression(player, job.getId()) == null) {
                                db.CreatePlayerJobInDb(player, job.getId());
                            }

                            RegisterJob(player, job);

                        } else {
                            player.sendMessage(ChatColor.RED + "Vous avez déjà un métier secondaire.");
                        }
                    }
                    break;
                case "leave":
                    if (args.length == 1) {
                        player.sendMessage(ChatColor.RED + "Veuillez selectionner un type de métier à quitter.");
                        return true;
                    }

                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Longueur de la commande non valide.");
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("principal")) {
                        // leave main job logic
                        if (!pProfile.hasMainJobs()) {
                            player.sendMessage(ChatColor.RED + "Vous n'avez pas de métier principal.");
                            return true;
                        }

                        LeaveJob(player, TRUE);
                        return true;
                    } else if (args[1].equalsIgnoreCase("secondaire")) {
                        // leave second job logic
                        if (!pProfile.hasSecondJobs()) {
                            player.sendMessage(ChatColor.RED + "Vous n'avez pas de métier secondaire.");
                            return true;
                        }

                        LeaveJob(player, FALSE);
                        return true;
                 } else {
                    player.sendMessage(ChatColor.RED + "Veuillez entrer un type de métier valide.");
                }
                break;

                default:
                    player.sendMessage(ChatColor.RED + "Utilisation de la commande :\n/<command> <argument> <argument>");
                    return true;
            } // end switch


        }
        return true;
    }

    private boolean IsCoolDownEnded(Player player, Job job, Duration coolDown) {
        // check cooldown
        Duration remainingDuration = getRemainingCooldown(player, job.getId(), coolDown);
        if (remainingDuration != null) {
            if (job.isPrimary()) {
                player.sendMessage(ChatColor.RED + "Le cooldown entre deux changement de métier principal n'est pas fini. " +
                        "Temps restant : " + remainingDuration.toDaysPart() + "j " +
                        remainingDuration.toHours() + "h " +
                        remainingDuration.toMinutesPart() + "m.");
            } else {
                player.sendMessage(ChatColor.RED + "Le cooldown entre deux changement de métier secondaire n'est pas fini. " +
                        "Temps restant : " + remainingDuration.toHours() + "h " +
                        remainingDuration.toMinutesPart() + "m.");
            }
            return false;
        }

        return true;
    }

    private void RegisterJob(Player p, Job job) {
        try {
            // update the jobs progress in db
            UpdatePlayerProfileInDB(_plugin, p);
            if (job.isPrimary()) {
                db.setPlayerMainJob(p, job.getId());
                p.sendMessage(ChatColor.GREEN + "Vous avez adopté métier principal de " + job.getDisplayName() + ".");
            } else {
                db.setPlayerSecondJob(p, job.getId());
                p.sendMessage(ChatColor.GREEN + "Vous avez adopté métier secondaire de " + job.getDisplayName() + ".");
            }

            if (db.getJobProgression(p, job.getId()) == null) {
                db.CreatePlayerJobInDb(p, job.getId());
            }

            // reset dynamically the PlayerProfile
            ResetPlayerProfile(p, _plugin);
        } catch (SQLException e) {
            _plugin.databaseApi.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[JobsPlugin] : Cannot register job.");
        }
    }

    private void LeaveJob(Player p, boolean primary) {
        try {
            // update the jobs progress in db
            UpdatePlayerProfileInDB(_plugin, p);
            if (primary) {
                ResetJobInfoCheckpoint(p, db.getPlayerMainJobFk(p));
                db.setPlayerMainJob(p, null);
                p.sendMessage(ChatColor.GREEN + "Vous avez quitter votre métier principal.");
            } else {
                ResetJobInfoCheckpoint(p, db.getPlayerSecondJobFk(p));
                db.setPlayerSecondJob(p, null);
                p.sendMessage(ChatColor.GREEN + "Vous avez quitter votre métier secondaire.");
            }

            // reset dynamically the PlayerProfile
            ResetPlayerProfile(p, _plugin);
        } catch (SQLException e) {
            _plugin.databaseApi.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[JobsPlugin] : Can't leave job.");
        }
    }

    private void ResetJobInfoCheckpoint(Player p, int pJobId) {
        // reset job to the last checkpoint
        JobProgression jobProgression = db.getJobProgression(p, pJobId);
        int lastCheckpoint = Arrays.stream(_jobLvlCheckpoint).filter(x -> x <= jobProgression.getLevel()).max().orElse(0);
        db.UpdateLastLeaveJob(p, pJobId);

        db.SetPlayerJobLvl(p, pJobId, lastCheckpoint);
        db.ResetPlayerJobExp(p, pJobId);
    }

    private Duration getRemainingCooldown(Player p, int jobId, Duration cooldown) {
        LocalDateTime lastLeave = db.getLastLeaveTime(p, jobId);
        if (lastLeave == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cooldownEnd = lastLeave.plus(cooldown);

        if (now.isAfter(cooldownEnd) || now.isEqual(cooldownEnd)) {
            return null;
        }

        return Duration.between(now, cooldownEnd);
    }
}
