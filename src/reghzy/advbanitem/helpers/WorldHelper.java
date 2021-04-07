package reghzy.advbanitem.helpers;

import net.minecraft.server.v1_6_R3.TileEntity;
import net.minecraft.server.v1_6_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;

import java.util.ArrayList;

public final class WorldHelper {
    public static ArrayList<net.minecraft.server.v1_6_R3.Chunk> getLoadedNotchChunks(World world) {
        ArrayList<net.minecraft.server.v1_6_R3.Chunk> chunks =
                new ArrayList<net.minecraft.server.v1_6_R3.Chunk>();
        if (world == null)
            return null;
        try {
            net.minecraft.server.v1_6_R3.World notchWorld =
                    WorldHelper.getWorldServer(world);
            for (Chunk chunk : world.getLoadedChunks()) {
                chunks.add(notchWorld.getChunkAt(chunk.getX(), chunk.getZ()));
            }
        }
        catch (Exception ignored) {

        }
        return chunks;
    }

    public static CraftWorld getCraftWorld(Chunk c){
        if (c == null)
            return null;
        return (CraftWorld) c.getWorld();
    }

    public static CraftWorld getCraftWorld(World w){
        if (w == null)
            return null;
        return (CraftWorld)w;
    }

    public static WorldServer getWorldServer(World w) {
        if (w == null)
            return null;
        return getCraftWorld(w).getHandle();
    }

    public static WorldServer getWorldServer(Chunk c) {
        if (c == null)
            return null;
        return getCraftWorld(c).getHandle();
    }

    public static Chunk getBukkitChunk(net.minecraft.server.v1_6_R3.Chunk chunk) {
        return chunk.bukkitChunk;
    }

    public static net.minecraft.server.v1_6_R3.Chunk getNotchChunk(Chunk chunk) {
        return getWorldServer(chunk).getChunkAt(chunk.getX(), chunk.getZ());
    }

    public static ArrayList<TileEntity> getTileEntities(World world, WorldServer worldServer) {
        ArrayList<TileEntity> tileEntities = new ArrayList<TileEntity>();
        for (Chunk chunk : world.getLoadedChunks()){
            for(BlockState blockState : chunk.getTileEntities()){
                TileEntity te = worldServer.getTileEntity(blockState.getX(), blockState.getY(), blockState.getZ());
                if (te != null) {
                    tileEntities.add(te);
                }
            }
        }
        return tileEntities;
    }

    public static boolean worldExists(String worldName){
        for(World world : Bukkit.getWorlds()){
            try{
                String name = world.getName();
                if (name.equalsIgnoreCase(worldName)){
                    return true;
                }
            }
            catch (Exception e) {

            }
        }
        return false;
    }

    public static World getWorldFromName(String worldName) {
        for (World world : Bukkit.getWorlds()) {
            try {
                String name = world.getName();
                if (name.equalsIgnoreCase(worldName)) {
                    return world;
                }
            }
            catch (Exception e) {

            }
        }
        return null;
    }
}
