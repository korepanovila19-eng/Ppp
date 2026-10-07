package com.example.shopmod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class Ui {
    public static MutableText t(String s, Formatting f) {
        return Text.literal(s).styled(st -> st.withItalic(false).withColor(f));
    }

    public static ItemStack button(Item item, String name, Formatting color, Text... lore) {
        ItemStack st = new ItemStack(item);
        st.setCustomName(t(name, color));
        addLore(st, Arrays.asList(lore));
        return st;
    }

    public static ItemStack filler() {
        ItemStack st = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        st.setCustomName(t(" ", Formatting.GRAY));
        return st;
    }

    public static void addLore(ItemStack st, List<? extends Text> lines) {
        if (lines.isEmpty()) return;
        NbtCompound display = st.getOrCreateSubNbt("display");
        NbtList list = display.contains("Lore", NbtElement.LIST_TYPE)
                ? display.getList("Lore", NbtElement.STRING_TYPE) : new NbtList();
        for (Text l : lines) list.add(NbtString.of(Text.Serializer.toJson(l)));
        display.put("Lore", list);
    }

    public static String money(long v) {
        return "$" + String.format(Locale.US, "%,d", v).replace(',', ' ');
    }
}
