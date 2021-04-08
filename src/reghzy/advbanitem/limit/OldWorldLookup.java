package reghzy.advbanitem.limit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class used for holding a map of block IDs which key
 * to a list of hash codes of disallowed world names.
 * <p>
 *     Basically, that
 * </p>
 */
public class OldWorldLookup {
    // Key: BlockID
    // Value: A list of disallowed world name hash codes
    // this is pretty much a multimap, but craftbukkit's multimap looks
    // a bit slow to im doin the manual solution ;))
    private static final HashMap<Integer, ArrayList<Integer>> blockWorldHashes;
    private static final HashMap<Integer, String> hashesToNames;

    public static void addBlockDisallowedWorld(Integer id, String worldName) {
        int worldHash = worldName.hashCode();
        ArrayList<Integer> worldNamesHashes = blockWorldHashes.get(id);
        if (worldNamesHashes == null) {
            worldNamesHashes = new ArrayList<Integer>();
            blockWorldHashes.put(id, worldNamesHashes);
        }
        worldNamesHashes.add(worldHash);
        hashesToNames.put(worldHash, worldName);
    }

    /**
     * Adds a disallowed world to the list, for the given block id
     */
    public static void addBlockDisallowedWorlds(Integer id, List<String> worlds) {
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(id);
        if (disallowedWorlds == null) {
            disallowedWorlds = new ArrayList<Integer>(worlds.size());
            blockWorldHashes.put(id, disallowedWorlds);
        }
        for (String worldName : worlds) {
            int hash = worldName.hashCode();
            disallowedWorlds.add(hash);
            hashesToNames.put(hash, worldName);
        }
    }

    /**
     * Adds a disallowed world to the list, for the given block id
     */
    public static void addBlockDisallowedWorldHashes(Integer id, List<Integer> worlds) {
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(id);
        if (disallowedWorlds == null) {
            disallowedWorlds = new ArrayList<Integer>(worlds.size());
            blockWorldHashes.put(id, disallowedWorlds);
        }
        disallowedWorlds.addAll(worlds);
    }

    /**
     * Removes a disallowed world from the list, for the given block id
     */
    public static void removeBlockDisallowedWorld(Integer blockId, String worldName) {
        removeBlockDisallowedWorld(blockId, worldName.hashCode());
    }

    /**
     * Removes a disallowed world from the list, for the given block id
     */
    public static void removeBlockDisallowedWorld(Integer blockId, Integer worldNameHash) {
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(blockId);
        if (disallowedWorlds == null) {
            return;
        }
        disallowedWorlds.remove(worldNameHash);
    }

    public static ArrayList<Integer> removeBlockDisallowedWorld(Integer id) {
        return blockWorldHashes.remove(id);
    }

    /**
     * Returns true if the given block id contains any disallowed worlds, and if that
     * list contains the given hashcode for a disallowed world name
     */
    public static boolean containsDisallowedWorld(Integer blockId, Integer worldNameHash) {
        ArrayList<Integer> disallowedWorlds = blockWorldHashes.get(blockId);
        if (disallowedWorlds == null || disallowedWorlds.size() == 0) {
            return false;
        }
        return disallowedWorlds.contains(worldNameHash);
    }

    /**
     * Returns true if the given block id contains any disallowed worlds, and if that
     * list contains the given world (this will call the world's hashcode function)
     */
    public static boolean containsDisallowedWorld(Integer blockId, String worldName) {
        return containsDisallowedWorld(blockId, worldName.hashCode());
    }

    public static ArrayList<Integer> getWorldsFromId(Integer id) {
        return blockWorldHashes.get(id);
    }

    public static ArrayList<String> getWorldNamesFromId(Integer id) {
        ArrayList<Integer> worldHashes = getWorldsFromId(id);
        if (worldHashes == null)
            return null;
        ArrayList<String> names = new ArrayList<String>(worldHashes.size());
        for(Integer hash : worldHashes) {
            String name = getNameFromHash(hash);
            if (name != null)
                names.add(name);
        }
        return names;
    }

    public static String getNameFromHash(int hash) {
        return hashesToNames.get(hash);
    }

    /**
     * Clears the list containing all of the block ids, and all of the disallowed
     * worlds linked to that block id
     */
    public static void clearBlockWorlds() {
        for(ArrayList<Integer> worldNameHashes : blockWorldHashes.values()) {
            worldNameHashes.clear();
        }
        blockWorldHashes.clear();
    }

    static {
        blockWorldHashes = new HashMap<Integer, ArrayList<Integer>>(32);
        hashesToNames = new HashMap<Integer, String>(32);
    }
}
