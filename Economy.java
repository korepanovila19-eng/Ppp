package com.example.shopmod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public final class Economy {
    public static long get(ServerPlayerEntity p) {
        return ShopState.get(p.getServer()).balance(p.getUuid());
    }

    public static void add(MinecraftServer server, UUID id, long delta) {
        ShopState.get(server).add(id, delta);
        ServerPlayerEntity online = server.getPlayerManager().getPlayer(id);
        if (online != null) Hud.update(online);
    }
}
