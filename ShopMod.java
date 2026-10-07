package com.example.shopmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ShopMod implements ModInitializer {
    public static final String ID = "shopmod";

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> Commands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Hud.init(handler.player));
        ServerLivingEntityEvents.AFTER_DEATH.register(Rewards::onDeath);
        ServerTickEvents.END_SERVER_TICK.register(server -> Scheduler.tick());
    }
}
