package fr.Valkyar.valkyarVaultPlugin.Commands;

import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class BalanceCommand implements CommandExecutor {

    private ValkyarVaultPlugin _plugin;

    public BalanceCommand(ValkyarVaultPlugin plugin) {
        this._plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            try {
                if (args.length == 1) {
                    player.sendMessage("Votre compte : " + _plugin.api.databaseLink.getPlayerBalanceByUUID(player));
                } else if (args.length == 2 && _plugin.api.databaseLink.getPlayerFk(Bukkit.getPlayer(args[0])) != null) {
                    player.sendMessage("Le joueur " + "");
                } else{

                }
                } catch (SQLException e) {
                ValkyarVaultPlugin.OutputColoredText(ValkyarVaultPlugin.RED_OUTPUT, "The player cannot be found.");
            }
        }

        return true;
    }
}
