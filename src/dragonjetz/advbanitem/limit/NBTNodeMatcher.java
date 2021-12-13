package dragonjetz.advbanitem.limit;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import dragonjetz.api.utils.text.StringHelper;

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
                throw new RuntimeException("There was no equals (=) or containment (~) sign!");
            }

            checkContains = true;
        }
        else {
            checkContains = false;
        }

        String nodes = full.substring(0, valueSplit);
        this.findValue = full.substring(valueSplit + 1).replace('_', ' ');
        this.nodes = new ArrayList<String>(Arrays.asList(nodes.split("\\.")));
    }

    public NBTMatchResult matchNbtTree(NBTTagCompound nbt) {

        // nbt contains "tank"
        // tank contains "FluidName" and "amount"
        // tank.FluidName=Blazing_Pythroeum

        if (this.nodes.size() == 0) {
            return NBTMatchResult.NBT_TREE_NOT_FOUND;
        }

        if (!nbt.func_74764_b(this.nodes.get(0))) {
            return NBTMatchResult.NBT_TREE_NOT_FOUND;
        }

        NBTBase next = nbt;
        int i = 0;
        int len = this.nodes.size();
        int endIndex = len - 1;
        for (String node : this.nodes) {
            if (next instanceof NBTTagCompound) {
                NBTTagCompound tagCompound = ((NBTTagCompound) next);
                if ((!node.startsWith("*") && node.length() < 2) && !tagCompound.func_74764_b(node)) {
                    return NBTMatchResult.NBT_MATCH_FAILED;
                }

                if (i == endIndex) {
                    String value = getNbtBaseData(tagCompound.func_74781_a(node));
                    if (value != null && match(value)) {
                        return NBTMatchResult.NBT_MATCH_SUCCESS;
                    }
                }
                else if (node.startsWith("*")) {
                    Integer index = StringHelper.parseInteger(node.substring(1));
                    if (index == null) {
                        return NBTMatchResult.NBT_TREE_NOT_FOUND;
                    }

                    Collection<NBTBase> values = tagCompound.func_74758_c();
                    if (index >= values.size()) {
                        return NBTMatchResult.NBT_TREE_NOT_FOUND;
                    }

                    int k = 0;
                    for (NBTBase base : values) {
                        if (k == index) {
                            if (!tagCompound.func_74764_b(base.func_74740_e())) {
                                return NBTMatchResult.NBT_TREE_NOT_FOUND;
                            }

                            next = tagCompound.func_74781_a(base.func_74740_e());
                            break;
                        }
                        k++;
                    }
                }
                else {
                    next = tagCompound.func_74781_a(node);
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

                    if (index >= list.func_74745_c()) {
                        return NBTMatchResult.NBT_TREE_NOT_FOUND;
                    }

                    next = list.func_74743_b(index);
                }
                else {
                    for (int j = 0, end = list.func_74745_c(); j < end; j++) {
                        NBTBase item = list.func_74743_b(j);
                        if ((item.func_74740_e().equalsIgnoreCase(node))) {
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
                    if (index < array.field_74749_a.length) {
                        if (match(String.valueOf(array.field_74749_a[index]))) {
                            return NBTMatchResult.NBT_MATCH_SUCCESS;
                        }
                    }
                }
                else {
                    NBTTagByteArray array = ((NBTTagByteArray) next);
                    if (index < array.field_74754_a.length) {
                        if(match(String.valueOf(array.field_74754_a[index]))) {
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
            return ((NBTTagString) base).field_74751_a;
        else if (base instanceof NBTTagByte)
            return String.valueOf(((NBTTagByte) base).field_74756_a);
        else if (base instanceof NBTTagShort)
            return String.valueOf(((NBTTagShort) base).field_74752_a);
        else if (base instanceof NBTTagInt)
            return String.valueOf(((NBTTagInt) base).field_74748_a);
        else if (base instanceof NBTTagLong)
            return String.valueOf(((NBTTagLong) base).field_74753_a);
        else if (base instanceof NBTTagFloat)
            return String.valueOf(((NBTTagFloat) base).field_74750_a);
        else if (base instanceof NBTTagDouble)
            return String.valueOf(((NBTTagDouble) base).field_74755_a);
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
