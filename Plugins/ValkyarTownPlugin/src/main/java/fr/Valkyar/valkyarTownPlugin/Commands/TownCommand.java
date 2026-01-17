package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCommand implements CommandExecutor {

    private ValkyarTownPlugin _plugin;
    public TownCommand(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            switch (args[0].toLowerCase()) {
                case "create":
                case "leave":
                    new CreateTownCommand(_plugin, player, command, s, args);
                    break;
                case "disband":
                    new DisbandTownCommand(_plugin, player, command, s, args);
                    break;
                case "claim":
                    new ClaimTownCommand(_plugin, player, command, s, args);
                    break;
                case "balance":
                    new TownBalanceCommand(_plugin, player, args);
                    break;
                default:
                    sender.sendMessage("erreur");
                    break;
            }
        }
        return true;
    }
}
