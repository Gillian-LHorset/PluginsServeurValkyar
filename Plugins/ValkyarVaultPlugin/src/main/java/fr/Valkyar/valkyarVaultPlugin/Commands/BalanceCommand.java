package fr.Valkyar.valkyarVaultPlugin.Commands;

import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
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
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player player) {
            try {
                player.sendMessage("Votre compte : " + _plugin.databaseLink.getPlayerBalanceByUUID(player));
            } catch (SQLException e) {
                ValkyarVaultPlugin.OutputColoredText(ValkyarVaultPlugin.RED_OUTPUT, "The player cannot be found.");
            }
        }

        return true;
    }
}
