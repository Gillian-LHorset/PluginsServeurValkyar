package fr.Valkyar.databaseCommunicationAPI.Utilities;

import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;

import java.util.List;

public class TownUtility {
    public static List<Town> getAllTowns(Database db, int town_id) {

        List<Town> towns = db.getAllTowns();

        return null;
    }
}
