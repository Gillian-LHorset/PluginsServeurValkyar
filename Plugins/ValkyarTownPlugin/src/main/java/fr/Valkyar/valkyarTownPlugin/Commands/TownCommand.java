package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class TownCommand implements CommandExecutor {

    private ValkyarTownPlugin _plugin;

    public TownCommand(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            PlayerProfile pp = playerProfiles.get(player.getUniqueId());
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Veuillez entrer des arguments à la commande.");
                return true;
            }
            if (args.length > 6) {
                player.sendMessage(ChatColor.RED + "La commande possède trop d'arguments.");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "create":
                    new CreateTownCommand(_plugin, player, command, s, args);
                    break;

                case "leave":
                    new LeaveTownCommand(_plugin, player, args);
                    break;

                case "disband":
                    if (pp.hasPermission("town_mayor")) {
                        new DisbandTownCommand(_plugin, player, command, s, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de disband la ville.");
                    }
                    break;

                case "claim":
                    if (pp.hasPermission("town_claim") || pp.hasHighPermission()) {
                        new ClaimTownCommand(_plugin, player, command, s, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de claim pour votre ville.");
                    }
                    break;

                case "unclaim":
                    if (pp.hasPermission("town_unclaim") || pp.hasHighPermission()) {
                        new UnclaimTownCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de unclaim pour votre ville.");
                    }
                    break;

                case "balance":
                    new TownBalanceCommand(_plugin, player, args);
                    break;

                case "deposit":
                    new TownDepositCommand(_plugin, player, args);
                    break;

                case "withdraw":
                    if (pp.hasPermission("town_withdraw") || pp.hasHighPermission()) {
                        new TownWithdrawCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de retier de l'argent à la ville.");
                    }
                    break;

                case "permission":
                    if (pp.hasPermission("town_mayor")) {
                        new GivePermissionCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas les permissions pour en accorder.");
                    }
                    break;

                case "invite":
                    if (pp.hasPermission("town_invite") || pp.hasHighPermission()) {
                        new InviteCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas les permissions d'inviter des gens à rejoindre la ville.");
                    }
                    break;

                case "kick":
                    if (pp.hasPermission("town_kick") || pp.hasHighPermission()) {
                        new KickTownCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas les permissions d'expulser quelqu'un de la ville.");
                    }
                    break;
                case "spawn":
                    new SpawnTown(_plugin, player, args);
                    break;
                case "avant-garde":
                    new AvantGardeCommand(_plugin, player, args);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
                    break;
                    //TODO : ajouter :
                    // ajouter des outposts,
                    // limite de membres,
                    // paliers de ville,
                    // toggle spawn mobs
                    // grades
                    // nombre de claim
            }
        }
        return true;
    }
}
