package fr.Valkyar.valkyarVaultPlugin.BlackMarket;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManageBlackMerchantCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player p) {
            BlackMerchant test = new BlackMerchant(p.getLocation());
        }
        return false;
    }
}
