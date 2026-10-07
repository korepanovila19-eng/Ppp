package com.example.shopmod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

import java.util.HashMap;
import java.util.Map;

/** Цены продажи системе (за 1 шт). Маленькие, как просили. Меняй здесь. */
public final class Prices {
    private static final Map<Item, Integer> P = new HashMap<>();

    private static void p(Item i, int v) {
        P.put(i, v);
    }

    static {
        p(Items.COBBLESTONE, 1); p(Items.STONE, 1); p(Items.COBBLED_DEEPSLATE, 1);
        p(Items.DIRT, 1); p(Items.SAND, 1); p(Items.GRAVEL, 1);
        p(Items.OBSIDIAN, 8);
        p(Items.COAL, 2); p(Items.REDSTONE, 2); p(Items.LAPIS_LAZULI, 2);
        p(Items.RAW_COPPER, 1); p(Items.COPPER_INGOT, 2);
        p(Items.RAW_IRON, 3); p(Items.IRON_INGOT, 5);
        p(Items.RAW_GOLD, 5); p(Items.GOLD_INGOT, 8);
        p(Items.QUARTZ, 3); p(Items.DIAMOND, 40); p(Items.EMERALD, 20);
        p(Items.NETHERITE_SCRAP, 50); p(Items.NETHERITE_INGOT, 400);
        p(Items.ROTTEN_FLESH, 1); p(Items.BONE, 1); p(Items.STRING, 1);
        p(Items.SPIDER_EYE, 2); p(Items.GUNPOWDER, 3); p(Items.FEATHER, 1);
        p(Items.LEATHER, 2); p(Items.SLIME_BALL, 3); p(Items.ARROW, 1);
        p(Items.ENDER_PEARL, 10); p(Items.BLAZE_ROD, 8); p(Items.GHAST_TEAR, 15);
        p(Items.GLOWSTONE_DUST, 2); p(Items.NETHER_WART, 2);
        p(Items.PHANTOM_MEMBRANE, 6); p(Items.PRISMARINE_SHARD, 2);
        p(Items.SHULKER_SHELL, 40); p(Items.ECHO_SHARD, 50);
        p(Items.DRAGON_BREATH, 30); p(Items.TOTEM_OF_UNDYING, 100);
        p(Items.WITHER_SKELETON_SKULL, 80); p(Items.NETHER_STAR, 500);
        p(Items.WHEAT, 1); p(Items.CARROT, 1); p(Items.POTATO, 1);
        p(Items.APPLE, 2); p(Items.BREAD, 2); p(Items.SUGAR_CANE, 1);
        p(Items.MELON_SLICE, 1); p(Items.PUMPKIN, 2);
        p(Items.BEEF, 1); p(Items.COOKED_BEEF, 3);
        p(Items.PORKCHOP, 1); p(Items.COOKED_PORKCHOP, 3);
        p(Items.CHICKEN, 1); p(Items.COOKED_CHICKEN, 2);
        p(Items.MUTTON, 1); p(Items.COOKED_MUTTON, 3);
        p(Items.GOLDEN_APPLE, 30); p(Items.ENCHANTED_GOLDEN_APPLE, 200);
    }

    /** Цена за 1 шт., 0 если система это не покупает. */
    public static int get(ItemStack st) {
        if (st.isEmpty() || st.hasNbt()) return 0;
        Integer v = P.get(st.getItem());
        if (v != null) return v;
        if (st.isIn(ItemTags.LOGS)) return 2;
        if (st.isIn(ItemTags.PLANKS)) return 1;
        if (st.isIn(ItemTags.WOOL)) return 2;
        return 0;
    }
}
