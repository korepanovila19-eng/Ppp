package com.example.shopmod;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Сохраняется в мире: балансы игроков и выставленные лоты. */
public class ShopState extends PersistentState {
    private static final long MAX_BALANCE = 1_000_000_000_000_000L;

    public final Map<UUID, Long> balances = new HashMap<>();
    public final List<Listing> listings = new ArrayList<>();
    public long nextId = 1;

    public static ShopState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager()
                .getOrCreate(ShopState::fromNbt, ShopState::new, ShopMod.ID);
    }

    public long balance(UUID id) {
        return balances.getOrDefault(id, 0L);
    }

    public void add(UUID id, long delta) {
        long v = Math.max(0L, Math.min(MAX_BALANCE, balance(id) + delta));
        balances.put(id, v);
        markDirty();
    }

    public Listing find(long id) {
        for (Listing l : listings) if (l.id == id) return l;
        return null;
    }

    public static ShopState fromNbt(NbtCompound nbt) {
        ShopState s = new ShopState();
        s.nextId = Math.max(1, nbt.getLong("nextId"));
        NbtList bl = nbt.getList("balances", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < bl.size(); i++) {
            NbtCompound c = bl.getCompound(i);
            s.balances.put(c.getUuid("id"), c.getLong("v"));
        }
        NbtList ll = nbt.getList("listings", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < ll.size(); i++) {
            Listing l = Listing.fromNbt(ll.getCompound(i));
            if (l != null) s.listings.add(l);
        }
        return s;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putLong("nextId", nextId);
        NbtList bl = new NbtList();
        for (Map.Entry<UUID, Long> e : balances.entrySet()) {
            NbtCompound c = new NbtCompound();
            c.putUuid("id", e.getKey());
            c.putLong("v", e.getValue());
            bl.add(c);
        }
        nbt.put("balances", bl);
        NbtList ll = new NbtList();
        for (Listing l : listings) ll.add(l.toNbt());
        nbt.put("listings", ll);
        return nbt;
    }
}
