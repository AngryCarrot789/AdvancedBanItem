package reghzy.advbanitem.limit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldLookup {
    // Key:   the hashcode of the item ID and Metadata (use getBlockHash())
    // Value: an array of disallowed world name hashes
    private static final HashMap<Integer, ArrayList<Integer>> blockWorldHashes;
    // Key:   the hashcode of the world name
    // value: the actual world name string
    private static final HashMap<Integer, String> hashesToName;

    public static void addDisallowed(int id, int meta, String worldName) {
        int blockHash = getBlockHash(id, meta);
        int worldHash = worldName.hashCode();
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(blockHash);
        if (disallowedWorlds == null) {
            disallowedWorlds = new ArrayList<Integer>(4);
            blockWorldHashes.put(id, disallowedWorlds);
        }
        disallowedWorlds.add(worldHash);
        hashesToName.put(worldHash, worldName);
    }

    public static void addDisallowed(int id, int meta, List<String> worlds) {
        int blockHash = getBlockHash(id, meta);
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(blockHash);
        if (disallowedWorlds == null) {
            disallowedWorlds = new ArrayList<Integer>(worlds.size());
            blockWorldHashes.put(blockHash, disallowedWorlds);
        }
        for (String worldName : worlds) {
            int worldHash = worldName.hashCode();
            disallowedWorlds.add(worldHash);
            hashesToName.put(worldHash, worldName);
        }
    }

    public static void addDisallowedHashes(int id, int meta, List<Integer> worlds) {
        int blockHash = getBlockHash(id, meta);
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(blockHash);
        if (disallowedWorlds == null) {
            disallowedWorlds = new ArrayList<Integer>(worlds.size());
            blockWorldHashes.put(blockHash, disallowedWorlds);
        }
        disallowedWorlds.addAll(worlds);
    }

    public static void removeDisallowed(int id, int meta) {
        int hash = getBlockHash(id, meta);
        blockWorldHashes.remove(hash);
    }

    public static void removeDisallowed(int id, int meta, String world) {
        removeDisallowed(id, meta, world.hashCode());
    }

    public static void removeDisallowed(int id, int meta, int worldHash) {
        int hash = getBlockHash(id, meta);
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(hash);
        if (disallowedWorlds == null) {
            return;
        }
        disallowedWorlds.remove(worldHash);
    }

    public static boolean containsWorld(int id, int meta, String world) {
        return containsWorld(id, meta, world.hashCode());
    }

    public static boolean containsWorld(int id, int meta, int worldHash) {
        int hash = getBlockHash(id, meta);
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(hash);
        if (disallowedWorlds == null || disallowedWorlds.size() == 0) {
            return false;
        }
        return disallowedWorlds.contains(worldHash);
    }

    public static void clearDisallowedWorlds() {
        for (ArrayList<Integer> worldNameHashes : blockWorldHashes.values()) {
            worldNameHashes.clear();
        }
        blockWorldHashes.clear();
    }

    public static String getWorldFromHash(int hash) {
        return hashesToName.get(hash);
    }

    public static int getBlockHash(int id, int meta) {
        return id + (meta << 16);
    }

    static {
        blockWorldHashes = new HashMap<Integer, ArrayList<Integer>>(128);
        hashesToName = new HashMap<Integer, String>(128);
    }
}
