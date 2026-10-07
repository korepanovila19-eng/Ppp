package com.example.shopmod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

/** Награды за убийство мобов. Меняй числа здесь. */
public final class Rewards {
    private static final int DEFAULT_MONSTER = 10;
    private static final Map<EntityType<?>, Integer> R = new HashMap<>();

    static {
        // обычные
        R.put(EntityType.ZOMBIE, 15);
        R.put(EntityType.HUSK, 15);
        R.put(EntityType.DROWNED, 15);
        R.put(EntityType.ZOMBIE_VILLAGER, 15);
        R.put(EntityType.SKELETON, 20);
        R.put(EntityType.STRAY, 20);
        R.put(EntityType.SPIDER, 15);
        R.put(EntityType.CAVE_SPIDER, 18);
        R.put(EntityType.CREEPER, 25);
        R.put(EntityType.SLIME, 3);
        R.put(EntityType.MAGMA_CUBE, 4);
        R.put(EntityType.SILVERFISH, 5);
        R.put(EntityType.ENDERMITE, 5);
        R.put(EntityType.VEX, 15);
        R.put(EntityType.PHANTOM, 25);
        R.put(EntityType.GUARDIAN, 30);
        R.put(EntityType.ZOMBIFIED_PIGLIN, 20);
        R.put(EntityType.HOGLIN, 30);
        R.put(EntityType.ZOGLIN, 40);
        // посложнее
        R.put(EntityType.ENDERMAN, 40);
        R.put(EntityType.BLAZE, 40);
        R.put(EntityType.PILLAGER, 30);
        R.put(EntityType.VINDICATOR, 40);
        R.put(EntityType.WITCH, 50);
        R.put(EntityType.SHULKER, 50);
        R.put(EntityType.GHAST, 60);
        R.put(EntityType.WITHER_SKELETON, 60);
        R.put(EntityType.EVOKER, 80);
        R.put(EntityType.PIGLIN_BRUTE, 100);
        R.put(EntityType.RAVAGER, 120);
        // боссы
        R.put(EntityType.ELDER_GUARDIAN, 2000);
        R.put(EntityType.WITHER, 5000);
        R.put(EntityType.WARDEN, 5000);
        R.put(EntityType.ENDER_DRAGON, 10000);
    }

    public static void onDeath(LivingEntity entity, DamageSource source) {
        if (!(source.getAttacker() instanceof ServerPlayerEntity player)) return;
        if (entity instanceof PlayerEntity) return;
        Integer reward = R.get(entity.getType());
        if (reward == null && entity instanceof Monster) reward = DEFAULT_MONSTER;
        if (reward == null) return;
        Economy.add(player.getServer(), player.getUuid(), reward);
        player.sendMessage(Text.literal("+" + Ui.money(reward) + " (" + entity.getName().getString() + ")")
                .formatted(Formatting.GOLD), true);
    }
}
