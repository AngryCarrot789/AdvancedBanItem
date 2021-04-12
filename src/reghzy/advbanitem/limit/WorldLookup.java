package reghzy.advbanitem.limit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains a map which maps the hashcode of an ID and Metadata to a list of disallowed world names.
 */
public final class WorldLookup {
    // Key:   the hashcode of the item ID and Metadata (use getBlockHash())
    // Value: an array of disallowed world name hashes
    private static final HashMap<Integer, ArrayList<String>> blockWorldHashes;

    private WorldLookup() { }

    public static void addDisallowed(int id, int meta, String worldName) {
        int blockHash = getBlockHash(id, meta);
        ArrayList<String> disallowedWorlds = blockWorldHashes.get(blockHash);
        if (disallowedWorlds == null) {
            disallowedWorlds = new ArrayList<String>(4);
            blockWorldHashes.put(id, disallowedWorlds);
        }
        disallowedWorlds.add(worldName);
    }

    public static void addDisallowed(int id, int meta, List<String> worlds) {
        int blockHash = getBlockHash(id, meta);
        ArrayList<String> disallowedWorlds = blockWorldHashes.get(blockHash);
        if (disallowedWorlds == null) {
            disallowedWorlds = new ArrayList<String>(worlds.size());
            blockWorldHashes.put(blockHash, disallowedWorlds);
        }
        disallowedWorlds.addAll(worlds);
    }

    public static void removeDisallowed(int id, int meta) {
        blockWorldHashes.remove(getBlockHash(id, meta));
    }

    public static void removeDisallowed(int id, int meta, String world) {
        int hash = getBlockHash(id, meta);
        ArrayList<String> disallowedWorlds = blockWorldHashes.get(hash);
        if (disallowedWorlds == null) {
            return;
        }
        disallowedWorlds.remove(world);
    }

    public static boolean containsWorld(int id, int meta, String worldHash) {
        int hash = getBlockHash(id, meta);
        ArrayList<String> disallowedWorlds = blockWorldHashes.get(hash);
        if (disallowedWorlds == null || disallowedWorlds.size() == 0) {
            return false;
        }
        return disallowedWorlds.contains(worldHash);
    }

    public static void clearDisallowedWorlds() {
        for (ArrayList<String> worldNameHashes : blockWorldHashes.values()) {
            worldNameHashes.clear();
        }
        blockWorldHashes.clear();
    }

    public static int getBlockHash(int id, int meta) {
        return id + (meta << 16);
    }

    // i havent tested these... but ithink this is how you extract data
    // within a number, AND the hash by the the number returns only
    // the bits which add to 1 or 2... sort of, 0 ignored
    public static int hashGetId(int hash) {
        return hash & 0xFF;
    }

    public static int hashGetMeta(int hash) {
        return hash & 0xFF00;
    }

    static {
        blockWorldHashes = new HashMap<Integer, ArrayList<String>>(128);
    }
}
