package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.Utilities.ChunkSystem;
import fr.Valkyar.databaseCommunicationAPI.managers.TownManager;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class CreateTownCommand {

    private final Double createTownPrice = 1.00;

    private ValkyarTownPlugin _plugin;
    private Database db;

    public CreateTownCommand(ValkyarTownPlugin plugin, CommandSender sender, Command command, String s, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (sender instanceof Player player) {
            PlayerProfile pp = playerProfiles.get(player.getUniqueId());

            Double playerBalance = pp.getCurrentMoney();

            if (args.length != 2 ) {
                player.sendMessage(ChatColor.RED + "Commande incorrecte.");
                return;
            }

            if (pp.hasTown()) {
                player.sendMessage(ChatColor.RED + "Vous appartenez déjà à une ville.");
                return;
            }

            Town playerTown = db.getPlayerTown(player);

            if (playerBalance < createTownPrice) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour créer une ville.");
                return;
            }

            if (playerTown != null) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer de ville, vous êtes déjà dans une ville.");
                return;

            }

            boolean townExists = TownMap.values().stream()
                    .anyMatch(town -> town.getName().equalsIgnoreCase(args[1]));

            if (townExists) {
                player.sendMessage(ChatColor.RED + "Une ville avec ce nom existe déjà.");
                return;
            }

            // check if it's in the town world
            if (!player.getWorld().getName().equalsIgnoreCase("world")) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer de ville dans ce monde.");
                return;
            }

            // check if the chunk is already claimed
            DatabaseCommunicationAPI.ChunkCoord claimedChunk = ChunkSystem.getChunkId(player.getLocation());
            if (ChunkSystem.isThisChunkClaim(claimedChunk)) {
                player.sendMessage(ChatColor.RED + "Ce chunk est déjà claim.");
                return;
            }

            TownManager.CreateTown(db, player, args[1]);
        }
    }
}
