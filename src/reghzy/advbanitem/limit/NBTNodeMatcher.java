package reghzy.advbanitem.limit;

import com.avaje.ebeaninternal.server.lib.util.InvalidDataException;
import net.minecraft.server.v1_6_R3.NBTBase;
import net.minecraft.server.v1_6_R3.NBTTagByte;
import net.minecraft.server.v1_6_R3.NBTTagByteArray;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagDouble;
import net.minecraft.server.v1_6_R3.NBTTagFloat;
import net.minecraft.server.v1_6_R3.NBTTagInt;
import net.minecraft.server.v1_6_R3.NBTTagIntArray;
import net.minecraft.server.v1_6_R3.NBTTagList;
import net.minecraft.server.v1_6_R3.NBTTagLong;
import net.minecraft.server.v1_6_R3.NBTTagShort;
import net.minecraft.server.v1_6_R3.NBTTagString;
import reghzy.advbanitem.helpers.StringHelper;
import sun.font.GlyphLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NBTNodeMatcher {
    public ArrayList<String> nodes;
    public String findValue;
    public boolean checkContains;

    public NBTNodeMatcher(String full) {
        int valueSplit = full.indexOf('=');
        if (valueSplit == -1) {
            valueSplit = full.indexOf('~');
            if (valueSplit == -1) {
                throw new InvalidDataException("There was no equals sign!");
            }

            checkContains = true;
        }
        else {
            checkContains = false;
        }

        String nodes = full.substring(0, valueSplit);
        this.findValue = full.substring(valueSplit + 1).replace('_', ' ');

        String[] split = nodes.split("\\.");
        if (split.length == 0) {
            return;
        }

        this.nodes = new ArrayList<String>(Arrays.asList(split));
    }

    public NBTMatchResult matchNbtTree(NBTTagCompound nbt) {

        // nbt contains "tank"
        // tank contains "FluidName" and "amount"
        // tank.FluidName=Blazing_Pythroeum

        if (this.nodes.size() == 0) {
            return NBTMatchResult.NBT_TREE_NOT_FOUND;
        }

        if (!nbt.hasKey(this.nodes.get(0))) {
            return NBTMatchResult.NBT_TREE_NOT_FOUND;
        }

        NBTBase next = nbt;
        int i = 0;
        int len = this.nodes.size();
        int endIndex = len - 1;
        for (String node : this.nodes) {
            if (next instanceof NBTTagCompound) {
                NBTTagCompound tagCompound = ((NBTTagCompound) next);
                if ((!node.startsWith("*") && node.length() < 2) && !tagCompound.hasKey(node)) {
                    return NBTMatchResult.NBT_MATCH_FAILED;
                }

                if (i == endIndex) {
                    String value = getNbtBaseData(tagCompound.get(node));
                    if (value != null && match(value)) {
                        return NBTMatchResult.NBT_MATCH_SUCCESS;
                    }
                }
                else {
                    if (node.startsWith("*")) {
                        Integer index = StringHelper.parseInteger(node.substring(1));
                        if (index == null) {
                            return NBTMatchResult.NBT_TREE_NOT_FOUND;
                        }

                        Collection keys = tagCompound.c();
                        if (index >= keys.size()) {
                            return NBTMatchResult.NBT_TREE_NOT_FOUND;
                        }

                        int k = 0;
                        for(Object key : keys) {
                            if (k == index) {
                                if (!tagCompound.hasKey(key.toString())) {
                                    return NBTMatchResult.NBT_TREE_NOT_FOUND;
                                }

                                next = tagCompound.get(key.toString());
                                break;
                            }
                            k++;
                        }
                    }
                    else {
                        next = tagCompound.get(node);
                    }
                }
            }
            else if (next instanceof NBTTagList) {
                NBTTagList list = ((NBTTagList) next);
                if (i == endIndex) {
                    return NBTMatchResult.NBT_TREE_NOT_FOUND;
                }

                if (node.startsWith("*")) {
                    Integer index = StringHelper.parseInteger(node.substring(1));
                    if (index == null) {
                        return NBTMatchResult.NBT_TREE_NOT_FOUND;
                    }

                    if (index >= list.size()) {
                        return NBTMatchResult.NBT_TREE_NOT_FOUND;
                    }

                    next = list.get(index);
                }
                else {
                    for (int j = 0, end = list.size(); j < end; j++) {
                        NBTBase item = list.get(j);
                        if ((item.getName().equalsIgnoreCase(node))) {
                            next = item;
                            break;
                        }
                    }
                }
            }
            else if (next instanceof NBTTagIntArray || next instanceof NBTTagByteArray) {
                Integer index = StringHelper.parseInteger(node);
                if (index == null || i != endIndex) {
                    return NBTMatchResult.NBT_TREE_NOT_FOUND;
                }

                if (next instanceof NBTTagIntArray) {
                    NBTTagIntArray array = ((NBTTagIntArray) next);
                    if (index < array.data.length) {
                        if (match(String.valueOf(array.data[index]))) {
                            return NBTMatchResult.NBT_MATCH_SUCCESS;
                        }
                    }
                }
                else {
                    NBTTagByteArray array = ((NBTTagByteArray) next);
                    if (index < array.data.length) {
                        if(match(String.valueOf(array.data[index]))) {
                            return NBTMatchResult.NBT_MATCH_SUCCESS;
                        }
                    }
                }

                return NBTMatchResult.NBT_MATCH_FAILED;
            }
            else {
                if (i != endIndex) {
                    return NBTMatchResult.NBT_TREE_NOT_FOUND;
                }

                String value = getNbtBaseData(next);
                if (value != null && match(value)) {
                    return NBTMatchResult.NBT_MATCH_SUCCESS;
                }
            }

            i++;
        }

        return NBTMatchResult.NBT_MATCH_FAILED;
    }

    public static String getNbtBaseData(NBTBase base) {
        if (base instanceof NBTTagString)
            return ((NBTTagString) base).data;
        else if (base instanceof NBTTagByte)
            return String.valueOf(((NBTTagByte) base).data);
        else if (base instanceof NBTTagShort)
            return String.valueOf(((NBTTagShort) base).data);
        else if (base instanceof NBTTagInt)
            return String.valueOf(((NBTTagInt) base).data);
        else if (base instanceof NBTTagLong)
            return String.valueOf(((NBTTagLong) base).data);
        else if (base instanceof NBTTagFloat)
            return String.valueOf(((NBTTagFloat) base).data);
        else if (base instanceof NBTTagDouble)
            return String.valueOf(((NBTTagDouble) base).data);
        else {
            // wut??? how can it not be one of these...
            // unless its the NBT end thing
            return null;
        }
    }

    public boolean match(String value) {
        if (this.checkContains) {
            return value.contains(this.findValue);
        }
        else {
            return value.equalsIgnoreCase(this.findValue);
        }
    }
}
