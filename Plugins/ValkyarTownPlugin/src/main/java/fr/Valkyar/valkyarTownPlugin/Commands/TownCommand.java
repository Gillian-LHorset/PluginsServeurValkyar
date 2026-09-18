package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
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
                    new CreateTownCommand(_plugin, player, command, s, args);
                    break;

                case "leave":
                    new LeaveTownCommand(_plugin, player, args);
                    break;

                case "disband":
                    if (player.hasPermission("town.mayor")) {
                        new DisbandTownCommand(_plugin, player, command, s, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de disband la ville.");
                    }
                    break;

                case "claim":
                    if (player.hasPermission("town.claim") || player.hasPermission("town.second") || player.hasPermission("town.mayor")) {
                        new ClaimTownCommand(_plugin, player, command, s, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de claim pour votre ville.");
                    }
                    break;

                case "unclaim":
                    if (player.hasPermission("town.unclaim") || player.hasPermission("town.second") || player.hasPermission("town.mayor")) {
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
                    if (player.hasPermission("town.withdraw") || player.hasPermission("town.second") || player.hasPermission("town.mayor")) {
                        new TownWithdrawCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de retier de l'argent à la ville.");
                    }
                    break;

                case "permission":
                    if (player.hasPermission("town.mayor")) {
                        new GivePermissionCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas les permissions pour en accorder.");
                    }
                    break;

                case "invite":
                    if (player.hasPermission("town.invite") || player.hasPermission("town.second") || player.hasPermission("town.mayor")) {
                        new InviteCommand(_plugin, player, args);
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas les permissions d'inviter des gens à rejoindre la ville.");
                    }
                    break;

                case "kick":
                    if (player.hasPermission("town.kick") || player.hasPermission("town.second") || player.hasPermission("town.mayor")) {
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
