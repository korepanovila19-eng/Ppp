package com.example.shopmod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.List;

/** Инвентарь игрока, отфильтрованный по категории. */
public class SellPickGui extends Gui {
    private final ServerPlayerEntity player;
    private final Category cat;

    public SellPickGui(ServerPlayerEntity p, Category cat) {
        super("Выставить: " + cat.title, 6);
        this.player = p;
        this.cat = cat;
    }

    @Override
    protected void render() {
        begin();
        int pos = 0;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack st = player.getInventory().getStack(slot);
            if (st.isEmpty() || Category.of(st) != cat) continue;
            ItemStack disp = st.copy();
            Ui.addLore(disp, List.of(Ui.t(" ", Formatting.GRAY),
                    Ui.t("Нажмите, чтобы выбрать цену", Formatting.GREEN)));
            final int invSlot = slot;
            set(pos++, disp, (p, b, t) -> go(p, new PriceGui(p, cat, invSlot)));
        }
        if (pos == 0) {
            set(22, Ui.button(Items.BARRIER, "В инвентаре нет предметов этой категории", Formatting.RED));
        }
        fillRange(45, 54);
        set(49, Ui.button(Items.ARROW, "Назад", Formatting.WHITE),
                (p, b, t) -> go(p, new CategoryGui(p, cat, false)));
    }
}
