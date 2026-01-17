package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.ChunkSystem;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ClaimTownCommand {

    private Database db;
    private ValkyarTownPlugin _plugin;

    private String _playerTown;

    private Double claimPrice = 1.00;
    private Double _townBalance;

    public ClaimTownCommand(ValkyarTownPlugin plugin, Player p, Command command, String s, String[] args) {

        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (args.length == 1) {
            _playerTown = db.getPlayerTown(p);
            if (_playerTown != null) {
                try {
                    _townBalance = db.getBalanceTownByName(_playerTown);
                    if (_townBalance > claimPrice) {
                        int[] claimedChunk = ChunkSystem.getChunkId(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
                        if (db.getChunkById(claimedChunk) == null) {
                            db.registerChunk(claimedChunk, _playerTown);
                            db.setTownBalanceDecreaseByName(_playerTown, claimPrice);
                            p.sendMessage(ChatColor.GREEN + "Le chunk à été claim.");
                        } else {
                            p.sendMessage(ChatColor.RED + "Ce chunk est déjà claim.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Vous n'avez pas l'argent suiffisante pour claim ce chunk.");
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
