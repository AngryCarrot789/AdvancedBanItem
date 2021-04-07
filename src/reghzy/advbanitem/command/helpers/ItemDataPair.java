package reghzy.advbanitem.command.helpers;

import org.bukkit.block.Block;

public class ItemDataPair {
    public int id;
    public int data;

    public ItemDataPair(int id, int data) {
        this.id = id;
        this.data = data;
    }

    public boolean match(Block block) {
        return block.getType().getId() == this.id && (this.data == -1 || block.getData() == this.data);
    }

    @Override
    public String toString() {
        return id + ":" + data;
    }
}