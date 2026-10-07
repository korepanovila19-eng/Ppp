package com.example.shopmod;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public final class Listing {
    public final long id;
    public final UUID seller;
    public final String sellerName;
    public final ItemStack stack;
    public final long price;

    public Listing(long id, UUID seller, String sellerName, ItemStack stack, long price) {
        this.id = id;
        this.seller = seller;
        this.sellerName = sellerName;
        this.stack = stack;
        this.price = price;
    }

    public NbtCompound toNbt() {
        NbtCompound c = new NbtCompound();
        c.putLong("id", id);
        c.putUuid("seller", seller);
        c.putString("name", sellerName);
        c.put("item", stack.writeNbt(new NbtCompound()));
        c.putLong("price", price);
        return c;
    }

    public static Listing fromNbt(NbtCompound c) {
        ItemStack st = ItemStack.fromNbt(c.getCompound("item"));
        if (st.isEmpty()) return null;
        return new Listing(c.getLong("id"), c.getUuid("seller"), c.getString("name"), st, c.getLong("price"));
    }
}
