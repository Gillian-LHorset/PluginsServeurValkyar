package fr.Valkyar.databaseCommunicationAPI.Models;

import java.sql.Timestamp;
import java.util.UUID;

public class BlockData {

    private UUID player;
    private String world;
    private int x;
    private int y;
    private int z;
    private String blockType;
    private Timestamp date;

    public BlockData(UUID Player, String World, int X, int Y, int Z, String BlockType, Timestamp Date) {
        player = Player;
        world = World;
        x = X;
        y = Y;
        z = Z;
        blockType = BlockType;
        date = Date;
    }

    public UUID getPlayer() {return player;}
    public String getWorld() {return world;}
    public int getX() {return x;}
    public int getY() {return y;}
    public int getZ() {return z;}
    public String getBlockType() {return blockType;}
    public Timestamp getDate() {
        if (date == null) {
            return null;
        }
        return date;
    }
}
