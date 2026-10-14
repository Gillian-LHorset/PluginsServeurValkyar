package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.ChunkSystem;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnTown {
    public SpawnTown(ValkyarTownPlugin plugin, Player p, String[] args) {
        Database db = plugin.databaseApi.databaseLink;
        Town town = db.getPlayerTown(p);
        Integer[] townSpawn;
        if (town.getName() != null) {
            if (args.length == 1 && args[0].equalsIgnoreCase("Spawn")) {
                // tp to the town spawn

                townSpawn = db.getSpawnTown(town);
                if (townSpawn[0] != null && townSpawn[1] != null && townSpawn[2] != null) {

                    World w = p.getWorld();
                    int x = townSpawn[0];
                    int y = townSpawn[1];
                    int z = townSpawn[2];
                    Location loc = new Location(w, x, y, z);

                    p.teleport(loc);

                    p.sendMessage(ChatColor.GREEN + "Téléportation éffectué.");
                } else {
                    p.sendMessage(ChatColor.RED + "Votre ville ne dispose pas de spawn.");
                    if (p.hasPermission("town.mayor") || p.hasPermission("town.second")) {
                        p.sendMessage(ChatColor.RED + "Vous pouvez définir un spawn avec la commande :\n/town spawn set");
                    }
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("Spawn") && args[1].equalsIgnoreCase("set")) {
                // set the town spawn
                if (db.isChunkFromTown(town, ChunkSystem.getChunkId(p.getLocation().getBlockX(), p.getLocation().getBlockZ()))) {
                    townSpawn = new Integer[]{p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()};
                    db.setSpawnTown(town, townSpawn);
                    p.sendMessage(ChatColor.GREEN + "Nouveau town spawn défini.");
                } else {
                    p.sendMessage(ChatColor.RED + "Error");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
        }
    }
}
