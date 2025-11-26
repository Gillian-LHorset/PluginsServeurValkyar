package fr.Valkyar.moderatorPlugin.Commands;

import fr.Valkyar.moderatorPlugin.ModeratorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import static fr.Valkyar.moderatorPlugin.ModeratorPlugin.InfiniteGlowing;

public class VanishCommand implements CommandExecutor {

    ModeratorPlugin plugin;

    public VanishCommand(ModeratorPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 0) {
                // if the player is in vanish
                if (plugin.VanishList.contains(player)) {
                    disableVanish(player, plugin);
                    OnVanishMod(player, false);
                } else {
                    plugin.VanishList.add(player);
                    player.sendMessage(ChatColor.GREEN + "You are in vanish mod.");
                    OnVanishMod(player, true);

                    // make he glows for other people in vanish mod
                    player.addPotionEffect(InfiniteGlowing);

                    // hide the player for everybody in the server
                    for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                        // if the player is already in vanish
                        if (plugin.VanishList.contains(playerOnline)) {
                            // the player other player in vanish is not hide to him
                            player.showPlayer(plugin, playerOnline);
                        } else {
                            // the player who execute the command is hide
                            playerOnline.hidePlayer(plugin, player);
                        }
                    }
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                // if nobody is in vanish
                if (plugin.VanishList.isEmpty()) {
                    player.sendMessage("There are no players in vanish mod.");
                } else {
                    // display the total of people in vanish mod
                    player.sendMessage("List of players in Vanish mod");

                    for (Player playerInVanish : plugin.VanishList) {
                        // display player in vanish mod
                        player.sendMessage(" - " + playerInVanish.getDisplayName()+"\n");
                    }
                }
            } else {
                // if the command have invalid argument
                player.sendMessage(ChatColor.RED + "Invalid arguments.");
                player.sendMessage("Usage: /vanish - /vanish <list>.");
            }
        }
        return true;
    }

    /// the sender become or not in vanish mod, defin by "passInVanish" when you call the methode
    ///     the player can fly, or not
    ///     the player cannot pick items, or he can
    public static void OnVanishMod (Player player, boolean passInVanish) {
        player.setAllowFlight(passInVanish);
        player.setCanPickupItems(!passInVanish);
        player.setInvulnerable(passInVanish);
    }

    // disable the vanish mode for the player in parameter with the list of player present in the plugin parameter
    public static void disableVanish(Player player, ModeratorPlugin plugin) {
        // remove the glowing
        player.removePotionEffect(PotionEffectType.GLOWING);

        // the player is no longer in vanish mod
        plugin.VanishList.remove(player);
        player.sendMessage(ChatColor.RED + "Vanish mod disable.");


        // hide other vanish player to him
        for (Player p : plugin.VanishList) {
            player.hidePlayer(plugin, p);
        }

        // show him to other player
        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            playerOnline.showPlayer(plugin, player);
        }
    }


}
