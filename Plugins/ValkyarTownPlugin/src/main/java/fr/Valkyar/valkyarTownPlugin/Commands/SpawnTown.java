package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.Utilities.ChunkSystem;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class SpawnTown {
    public SpawnTown(ValkyarTownPlugin plugin, Player p, String[] args) {
        Database db = plugin.databaseApi.databaseLink;

        PlayerProfile pp = playerProfiles.get(p.getUniqueId());

        if (!pp.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            return;
        }
        Town town = pp.getTown();

        if (args.length != 1 && args.length != 2) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
            return;
        }

        if (args.length == 1) {
            // tp to the town spawn
            if (!town.hasSpawn()) {
                p.sendMessage(ChatColor.RED + "Votre ville ne dispose pas de spawn.");
                if (p.hasPermission("town_mayor") || p.hasPermission("town_second")) {
                    p.sendMessage(ChatColor.RED + "Vous pouvez définir un spawn avec la commande :\n/town spawn set");
                }
                return;
            }
            p.teleport(town.getSpawnLocation());

            p.sendMessage(ChatColor.GREEN + "Téléportation éffectué.");
        }


        if (args.length == 2) {
            if (!args[1].equalsIgnoreCase("set")) {
                p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
                return;
            }
            // set the town spawn
            if (!town.hasChunk(ChunkSystem.getChunkId(p.getLocation()))) {
                p.sendMessage(ChatColor.RED + "Vous ne pouvez pas définir un spawn dans un chunk qui n'appartien pas à votre ville.");
                return;
            }

            if (!pp.hasHighPermission()) {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de déplacer le spawn.");
                return;
            }

            town.setTownSpawnLocation(p.getLocation());
            p.sendMessage(ChatColor.GREEN + "Nouveau town spawn défini.");

        }
    }
}

