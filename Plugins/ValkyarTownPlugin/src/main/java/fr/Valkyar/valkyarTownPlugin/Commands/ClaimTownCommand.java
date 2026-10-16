package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.Utilities.ChunkSystem;
import fr.Valkyar.databaseCommunicationAPI.managers.TownManager;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class ClaimTownCommand {

    private Database db;
    private ValkyarTownPlugin _plugin;

    private Town _playerTown;

    private Double claimPrice = 1.00; // TODO : définir un vrai montant
    private Double _townBalance;
    public ClaimTownCommand(ValkyarTownPlugin plugin, Player p, Command command, String s, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
        PlayerProfile profile = playerProfiles.get(p.getUniqueId());

        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Commande incorrecte.");
            return;
        }

        if (!profile.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            return;
        }
        _playerTown = profile.getTown();

        _playerTown.getBalance();
        if (_playerTown.getBalance() < claimPrice) {
            p.sendMessage(ChatColor.RED + "Votre ville n'a pas l'argent suffisante pour claim ce chunk.");
            return;
        }

        if (!p.getWorld().getName().equalsIgnoreCase("world")) {
            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas claim dans ce monde.");
            return;
        }

        DatabaseCommunicationAPI.ChunkCoord claimedChunk = ChunkSystem.getChunkId(p.getLocation());
        if (ChunkSystem.isThisChunkClaim(claimedChunk)) {
            p.sendMessage(ChatColor.RED + "Ce chunk est déjà claim.");
            return;
        }

        if (!ChunkSystem.isChunkNextToAnotherTownClaim(p, db, claimedChunk, _playerTown)) {
            p.sendMessage(ChatColor.RED + "Le chunk ciblé doit être adjacent à un autre chunk de la ville.");
            return;
        }

        TownManager.TownClaim(DatabaseCommunicationAPI.getApi(), _playerTown, claimPrice, claimedChunk);

        p.sendMessage(ChatColor.GREEN + "Le chunk à été claim.");

    }
}
