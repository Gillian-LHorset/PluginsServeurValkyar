package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.ChunkSystem;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class UnclaimTownCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;

    private String _playerTown;

    public UnclaimTownCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (args.length == 1) {
            _playerTown = db.getPlayerTown(p);
            if (_playerTown != null) {
                try {
                    int[] unclaimedChunk = ChunkSystem.getChunkId(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
                    if (db.getChunkById(unclaimedChunk) != null) {
                        db.deleteOneChunksById(_playerTown, unclaimedChunk);
                        p.sendMessage(ChatColor.GREEN + "Le chunk à été unclaim.");
                    } else {
                        p.sendMessage(ChatColor.RED + "Ce chunk n'est pas claim.");
                    }
                } catch (SQLException e) {
                    p.sendMessage(ChatColor.RED + "Une erreur s'est produite.");
                    throw new RuntimeException(e);
                }
            }  else {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            }
        }

    }
}
