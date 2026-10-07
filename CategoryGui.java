package com.example.shopmod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class CategoryGui extends Gui {
    private final ServerPlayerEntity player;
    private final Category cat; // null для "Мои лоты"
    private final boolean mine;
    private int page = 0;

    public CategoryGui(ServerPlayerEntity p, Category cat, boolean mine) {
        super(mine ? "Мои лоты" : "Магазин: " + cat.title, 6);
        this.player = p;
        this.cat = cat;
        this.mine = mine;
    }

    @Override
    protected void render() {
        begin();
        ShopState st = ShopState.get(player.getServer());
        List<Listing> list = new ArrayList<>();
        for (int i = st.listings.size() - 1; i >= 0; i--) {
            Listing l = st.listings.get(i);
            if (mine) {
                if (!l.seller.equals(player.getUuid())) continue;
            } else if (Category.of(l.stack) != cat) continue;
            list.add(l);
        }
        int pages = Math.max(1, (list.size() + 44) / 45);
        if (page >= pages) page = pages - 1;
        if (page < 0) page = 0;

        for (int i = 0; i < 45; i++) {
            int idx = page * 45 + i;
            if (idx >= list.size()) break;
            Listing l = list.get(idx);
            boolean own = l.seller.equals(player.getUuid());
            ItemStack disp = l.stack.copy();
            Ui.addLore(disp, List.of(
                    Ui.t(" ", Formatting.GRAY),
                    Ui.t("Цена: " + Ui.money(l.price), Formatting.GOLD),
                    Ui.t("Продавец: " + l.sellerName, Formatting.GRAY),
                    own ? Ui.t("Ваш лот - нажмите, чтобы снять", Formatting.YELLOW)
                        : Ui.t("Нажмите, чтобы купить", Formatting.GREEN)));
            final long id = l.id;
            set(i, disp, (p, b, t) -> go(p, new ConfirmGui(p, id)));
        }

        fillRange(45, 54);
        set(45, Ui.button(Items.ARROW, "Назад", Formatting.WHITE), (p, b, t) -> go(p, new ShopGui(p)));
        if (page > 0) {
            set(46, Ui.button(Items.SPECTRAL_ARROW, "Предыдущая страница", Formatting.YELLOW),
                    (p, b, t) -> { page--; refresh(); });
        }
        if (page < pages - 1) {
            set(52, Ui.button(Items.SPECTRAL_ARROW, "Следующая страница", Formatting.YELLOW),
                    (p, b, t) -> { page++; refresh(); });
        }
        if (!mine) {
            set(49, Ui.button(Items.EMERALD_BLOCK, "Выставить", Formatting.GREEN,
                            Ui.t("Продать предмет категории \"" + cat.title + "\"", Formatting.GRAY)),
                    (p, b, t) -> go(p, new SellPickGui(p, cat)));
        }
        set(53, Ui.button(Items.GOLD_INGOT, "Баланс: " + Ui.money(st.balance(player.getUuid())), Formatting.GOLD));
    }
}
