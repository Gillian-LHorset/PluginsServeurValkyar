package fr.Valkyar.databaseCommunicationAPI.Utilities;

import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;

import java.util.List;

public class TownUtility {
    public static List<Town> getAllTowns(Database db) {

        List<Town> towns = db.getAllTownsBasics();

        towns.forEach(t -> {
            t.setClaimedChunk(db.getAllTownChunkOfTown(t));
            t.setAvantGardes(db.getTownAvantGardes(t));
            t.getAvantGardes().forEach(a -> {
                db.getAvantGardeChunks(a);
            });
        });

        return towns;
    }
}
