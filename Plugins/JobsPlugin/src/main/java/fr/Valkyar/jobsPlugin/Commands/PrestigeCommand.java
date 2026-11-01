package fr.Valkyar.jobsPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.Commands.prestigeCommands.PrestigeShop;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class PrestigeCommand implements CommandExecutor {
    private final JobsPlugin _plugin;
    private final FileConfiguration _config;
    private PrestigeShop _shop;
    public PrestigeCommand(JobsPlugin plugin, FileConfiguration config) {
        this._plugin = plugin;
        this._config = config;
        _shop = new PrestigeShop(_plugin, _config);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length != 0) {
                p.sendMessage(ChatColor.RED + "Erreur de syntaxe dans la commande.\nUtilisation correcte : /<commande>.");
                return true;
            }

            PlayerProfile pP = playerProfiles.get(p.getUniqueId());

            if (!pP.hasSpecialisation()) {
                p.sendMessage(ChatColor.RED + "Vous ne pouvez pas  utiliser cette commande sans spécialisation.");
                return true;
            }

            if (args.length == 0) {
                _shop.openPrestigeShop(p);
                return true;
            }

        }

        return true;
    }
}
