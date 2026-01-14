package fr.Valkyar.valkyarTownPlugin.Utility;

public class ChunkSystem {

    public static int[] getChunkId(Integer x, int z) {
        x = Math.floorDiv(x, 16) + 1;
        z = Math.floorDiv(z, 16) + 1;

        return new int[]{x ,z};
    }
}
