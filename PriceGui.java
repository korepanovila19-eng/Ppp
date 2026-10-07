package com.example.shopmod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

/** Меню выбора количества и цены. */
public class PriceGui extends Gui {
    private static final long MAX_PRICE = 1_000_000_000L;
    private static final long[] STEPS = {1, 10, 100, 1000, 10000, 100000};

    private final ServerPlayerEntity player;
    private final Category cat;
    private final int slot;
    private final ItemStack template;
    private final int max;
    private int amount;
    private long price = 100;

    public PriceGui(ServerPlayerEntity p, Category cat, int slot) {
        super("Цена за лот", 4);
        this.player = p;
        this.cat = cat;
        this.slot = slot;
        ItemStack cur = p.getInventory().getStack(slot);
        this.template = cur.copy();
        this.max = Math.max(1, cur.getCount());
        this.amount = this.max;
    }

    private void changeAmount(int d) {
        amount = Math.max(1, Math.min(max, amount + d));
        refresh();
    }

    private void changePrice(long d) {
        price = Math.max(1, Math.min(MAX_PRICE, price + d));
        refresh();
    }

    @Override
    protected void render() {
        begin();
        ItemStack shown = template.copy();
        shown.setCount(amount);
        set(4, shown);
        set(2, Ui.button(Items.RED_STAINED_GLASS_PANE, "Количество -16", Formatting.RED), (p, b, t) -> changeAmount(-16));
        set(3, Ui.button(Items.RED_STAINED_GLASS_PANE, "Количество -1", Formatting.RED), (p, b, t) -> changeAmount(-1));
        set(5, Ui.button(Items.LIME_STAINED_GLASS_PANE, "Количество +1", Formatting.GREEN), (p, b, t) -> changeAmount(1));
        set(6, Ui.button(Items.LIME_STAINED_GLASS_PANE, "Количество +16", Formatting.GREEN), (p, b, t) -> changeAmount(16));

        for (int i = 0; i < STEPS.length; i++) {
            final long s = STEPS[i];
            set(10 + i, Ui.button(Items.LIME_STAINED_GLASS_PANE, "+" + s, Formatting.GREEN), (p, b, t) -> changePrice(s));
            set(19 + i, Ui.button(Items.RED_STAINED_GLASS_PANE, "-" + s, Formatting.RED), (p, b, t) -> changePrice(-s));
        }

        set(29, Ui.button(Items.BARRIER, "Отмена", Formatting.RED),
                (p, b, t) -> go(p, new CategoryGui(p, cat, false)));
        set(31, Ui.button(Items.GOLD_INGOT, "Цена: " + Ui.money(price), Formatting.GOLD,
                Ui.t("Количество: " + amount, Formatting.GRAY)));
        set(33, Ui.button(Items.EMERALD_BLOCK, "Выставить за " + Ui.money(price), Formatting.GREEN),
                (p, b, t) -> confirm(p));
        fillEmpty();
    }

    private void confirm(ServerPlayerEntity p) {
        ShopState st = ShopState.get(p.getServer());
        ItemStack cur = p.getInventory().getStack(slot);
        if (cur.isEmpty() || !ItemStack.canCombine(cur, template) || cur.getCount() < amount) {
            p.sendMessage(Ui.t("Предмет в инвентаре изменился, попробуйте снова.", Formatting.RED));
            go(p, new CategoryGui(p, cat, false));
            return;
        }
        long mineCount = st.listings.stream().filter(l -> l.seller.equals(p.getUuid())).count();
        if (mineCount >= Market.MAX_LISTINGS) {
            p.sendMessage(Ui.t("Лимит лотов: " + Market.MAX_LISTINGS, Formatting.RED));
            return;
        }
        cur.decrement(amount);
        ItemStack item = template.copy();
        item.setCount(amount);
        st.listings.add(new Listing(st.nextId++, p.getUuid(), p.getName().getString(), item, price));
        st.markDirty();
        p.sendMessage(Ui.t("Выставлено: " + item.getName().getString() + " x" + amount
                + " за " + Ui.money(price), Formatting.GREEN));
        go(p, new CategoryGui(p, Category.of(item), false));
    }
}
