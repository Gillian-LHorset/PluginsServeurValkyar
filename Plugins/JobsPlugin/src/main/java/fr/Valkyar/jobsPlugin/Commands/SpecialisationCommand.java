package fr.Valkyar.jobsPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.time.Duration;
import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.specialisationsList;
import static fr.Valkyar.databaseCommunicationAPI.managers.SpecialisationManager.setPlayerSpecialisation;

public class SpecialisationCommand implements CommandExecutor {

    private JobsPlugin _plugin;
    private Database db;

    public SpecialisationCommand(JobsPlugin plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player p) {
            if (args.length != 2) {
                p.sendMessage(ChatColor.RED + "Erreur dans la commande.\nUtilisation : /<commande> <argument> <argument>");
                return true;
            }

            switch (args[0]) {
                case "choisir":
                    PlayerProfile pP = playerProfiles.get(p.getUniqueId());

                    if (!pP.hasMainJobs()) {
                        p.sendMessage(ChatColor.RED + "Vous n'avez pas de métier principal.");
                        return true;
                    }

                    if (pP.getPrimaryJobLvl() < 100) {
                        p.sendMessage(ChatColor.RED + "Vous n'avez pas encore atteint le niveau 100.");
                        return true;
                    }

                    if (pP.hasSpecialisation()) {
                        p.sendMessage(ChatColor.RED + "Vous avez déjà choisi une spécialisation.");
                        return true;
                    }

                    Database.specialisation spe = specialisationsList.stream()
                            .filter(speRecord -> speRecord.name().equalsIgnoreCase(args[1]))
                            .findFirst()
                            .orElse(null);

                    if (spe == null) {
                        p.sendMessage(ChatColor.RED + "Cette spécialisation n'existe pas pour votre métier.");
                        return true;
                    }

                    pP.getProgression(pP.getPrimaryJob().getId()).setSpecialisation(spe.name());
                    p.sendMessage(ChatColor.GREEN + "Vous avez choisi la spécialisation " + spe.name() + " pour le métier de " + pP.getPrimaryJob().getDisplayName() + ".");
                    setPlayerSpecialisation(_plugin.databaseApi, p, spe);
                    break;
                default:
                    p.sendMessage(ChatColor.RED + "Argument incorrect.");
                    return true;
            }
        }

        return true;
    }
}
