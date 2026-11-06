package fr.Valkyar.valkyarVaultPlugin.BlackMarket;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin.blackMerchant;

public class ManageBlackMerchantCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player p) {
            blackMerchant.CreateBlackMerchant(p.getLocation());
        }
        return false;
    }
}
