package com.example.shopmod;

import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class ShopGui extends Gui {
    private final ServerPlayerEntity player;

    public ShopGui(ServerPlayerEntity p) {
        super("Магазин", 3);
        this.player = p;
    }

    @Override
    protected void render() {
        begin();
        ShopState st = ShopState.get(player.getServer());
        Category[] cs = Category.values();
        for (int i = 0; i < cs.length; i++) {
            Category c = cs[i];
            long n = st.listings.stream().filter(l -> Category.of(l.stack) == c).count();
            set(10 + i, Ui.button(c.icon, c.title, Formatting.YELLOW,
                            Ui.t("Лотов: " + n, Formatting.GRAY),
                            Ui.t("Нажмите, чтобы открыть", Formatting.GREEN)),
                    (p, b, t) -> go(p, new CategoryGui(p, c, false)));
        }
        set(4, Ui.button(Items.GOLD_INGOT, "Баланс: " + Ui.money(st.balance(player.getUuid())), Formatting.GOLD));
        set(22, Ui.button(Items.CHEST, "Мои лоты", Formatting.AQUA,
                        Ui.t("Снять свои товары с продажи", Formatting.GRAY)),
                (p, b, t) -> go(p, new CategoryGui(p, null, true)));
        fillEmpty();
    }
}
