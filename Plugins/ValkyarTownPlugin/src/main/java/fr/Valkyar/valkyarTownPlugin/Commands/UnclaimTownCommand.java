package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.Utilities.ChunkSystem;
import fr.Valkyar.databaseCommunicationAPI.managers.TownManager;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class UnclaimTownCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;

    private Town _playerTown;

    public UnclaimTownCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
            return;
        }

        PlayerProfile pp = playerProfiles.get(p.getUniqueId());

        if (!pp.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            return;
        }

        _playerTown = pp.getTown();

        DatabaseCommunicationAPI.ChunkCoord unclaimedChunk = ChunkSystem.getChunkId(p.getLocation());
        if (!_playerTown.hasChunk(unclaimedChunk)) {
            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas unclaim un chunk qui n'appartient pas à votre ville.");
            return;
        }
        TownManager.TownUnclaim(DatabaseCommunicationAPI.getApi(), _playerTown, unclaimedChunk);
        p.sendMessage(ChatColor.GREEN + "Le chunk à été unclaim.");

    }
}
