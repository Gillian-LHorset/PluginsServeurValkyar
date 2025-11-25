package fr.Valkyar.moderatorPlugin.Commands;

import fr.Valkyar.moderatorPlugin.ModeratorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    ModeratorPlugin plugin;

    public VanishCommand(ModeratorPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (plugin.VanishList.contains(player)) {
                    plugin.VanishList.remove(player);
                    player.sendMessage(ChatColor.RED + "Vanish mod disable.");
                    OnVanishMod(player, false);
                    for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                        playerOnline.showPlayer(plugin, player);
                    }
                } else {
                    plugin.VanishList.add(player);
                    player.sendMessage(ChatColor.GREEN + "You are in vanish mod.");
                    OnVanishMod(player, true);

                    for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                        playerOnline.hidePlayer(plugin, player);
                    }
                }
            }

        }

        return true;
    }

    /// the sender become or not in vanish mod, defin by "passInVanish"
    private void OnVanishMod (Player player, boolean passInVanish) {
        player.setAllowFlight(passInVanish);
        player.setCanPickupItems(!passInVanish);
    }

}
