package fr.Valkyar.databaseCommunicationAPI.managers;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class SpecialisationManager {
    public static void setPlayerSpecialisation(DatabaseCommunicationAPI plugin, Player p, Database.specialisation spe) {
        PlayerProfile pP = playerProfiles.get(p.getUniqueId());
        pP.getProgression(pP.getPrimaryJob().getId()).setSpecialisation(spe.name());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.databaseLink.setSpecialisation(p, spe, playerProfiles.get(p.getUniqueId()).getPrimaryJob());
        });
    }
}
