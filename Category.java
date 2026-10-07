package com.example.shopmod;

import net.minecraft.item.*;

public enum Category {
    FOOD("Еда", Items.COOKED_BEEF),
    WEAPONS("Оружие", Items.DIAMOND_SWORD),
    ARMOR("Броня", Items.DIAMOND_CHESTPLATE),
    TOOLS("Инструменты", Items.DIAMOND_PICKAXE),
    BLOCKS("Блоки", Items.GRASS_BLOCK),
    POTIONS("Зелья", Items.POTION),
    MISC("Разное", Items.ENDER_PEARL);

    public final String title;
    public final Item icon;

    Category(String title, Item icon) {
        this.title = title;
        this.icon = icon;
    }

    public static Category of(ItemStack stack) {
        Item i = stack.getItem();
        if (i instanceof SwordItem || i instanceof BowItem || i instanceof CrossbowItem
                || i instanceof TridentItem || i instanceof ArrowItem) return WEAPONS;
        if (i instanceof ArmorItem || i instanceof ShieldItem || i instanceof ElytraItem) return ARMOR;
        if (i instanceof ToolItem || i instanceof FishingRodItem || i instanceof ShearsItem
                || i instanceof FlintAndSteelItem) return TOOLS;
        if (i.isFood()) return FOOD;
        if (i instanceof PotionItem) return POTIONS;
        if (i instanceof BlockItem) return BLOCKS;
        return MISC;
    }
}
