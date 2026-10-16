package fr.Valkyar.valkyarTownPlugin.JoinInvitationCommand;

import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RefuseCommand implements CommandExecutor {

    private ValkyarTownPlugin _plugin;

    public RefuseCommand(ValkyarTownPlugin plugin) {
        _plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player p) {
            if (!_plugin.sentInvitations.containsKey(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas d'invitation en cours.");
            }

            Player pSender = Bukkit.getPlayer(_plugin.sentInvitations.get(p.getUniqueId()));
            if (pSender != null && pSender.isOnline()) {
                pSender.sendMessage(ChatColor.RED + p.getName() + " a refusé votre invitation.");
            }

            _plugin.sentInvitations.remove(p.getUniqueId());
            p.sendMessage("Vous avez refusé l'invitation.");

            return true;
        }
        return false;
    }
}
