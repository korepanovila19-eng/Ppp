package com.example.shopmod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.List;

/** Продажа предметов системе по низким ценам. ЛКМ - весь стак, ПКМ - 1 шт. */
public class SellItemsGui extends Gui {
    private final ServerPlayerEntity player;

    public SellItemsGui(ServerPlayerEntity p) {
        super("Продажа системе", 6);
        this.player = p;
    }

    private void sell(ServerPlayerEntity p, int slot, int amount) {
        ItemStack st = p.getInventory().getStack(slot);
        int per = Prices.get(st);
        if (per <= 0) return;
        int n = amount < 0 ? st.getCount() : Math.min(amount, st.getCount());
        long total = (long) per * n;
        st.decrement(n);
        Economy.add(p.getServer(), p.getUuid(), total);
        p.sendMessage(Ui.t("+" + Ui.money(total), Formatting.GOLD), true);
    }

    @Override
    protected void render() {
        begin();
        long all = 0;
        int pos = 0;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack st = player.getInventory().getStack(slot);
            int per = Prices.get(st);
            if (per <= 0) continue;
            all += (long) per * st.getCount();
            ItemStack disp = st.copy();
            Ui.addLore(disp, List.of(Ui.t(" ", Formatting.GRAY),
                    Ui.t("За 1 шт.: " + Ui.money(per), Formatting.GOLD),
                    Ui.t("Весь стак: " + Ui.money((long) per * st.getCount()), Formatting.GOLD),
                    Ui.t("ЛКМ - продать всё", Formatting.GREEN),
                    Ui.t("ПКМ - продать 1 шт.", Formatting.GREEN)));
            final int invSlot = slot;
            set(pos++, disp, (p, b, t) -> {
                sell(p, invSlot, b == 1 ? 1 : -1);
                refresh();
            });
        }
        if (pos == 0) {
            set(22, Ui.button(Items.BARRIER, "Нечего продавать системе", Formatting.RED));
        }
        fillRange(45, 54);
        set(49, Ui.button(Items.HOPPER, "Продать всё", Formatting.GREEN,
                Ui.t("Итого: " + Ui.money(all), Formatting.GOLD)), (p, b, t) -> {
            for (int s = 0; s < 36; s++) sell(p, s, -1);
            refresh();
        });
        set(53, Ui.button(Items.GOLD_INGOT,
                "Баланс: " + Ui.money(ShopState.get(player.getServer()).balance(player.getUuid())), Formatting.GOLD));
    }
}
