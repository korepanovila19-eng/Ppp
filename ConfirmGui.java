package com.example.shopmod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.List;

/** Подтверждение покупки (или снятия своего лота). */
public class ConfirmGui extends Gui {
    private final ServerPlayerEntity player;
    private final long id;

    public ConfirmGui(ServerPlayerEntity p, long id) {
        super("Подтверждение", 3);
        this.player = p;
        this.id = id;
    }

    @Override
    protected void render() {
        begin();
        Listing l = ShopState.get(player.getServer()).find(id);
        if (l == null) {
            set(13, Ui.button(Items.BARRIER, "Лот уже продан или снят", Formatting.RED),
                    (p, b, t) -> go(p, new ShopGui(p)));
            fillEmpty();
            return;
        }
        boolean own = l.seller.equals(player.getUuid());
        String name = l.stack.getName().getString() + " x" + l.stack.getCount();

        ItemStack disp = l.stack.copy();
        Ui.addLore(disp, List.of(Ui.t(" ", Formatting.GRAY),
                Ui.t("Цена: " + Ui.money(l.price), Formatting.GOLD),
                Ui.t("Продавец: " + l.sellerName, Formatting.GRAY)));
        set(13, disp);

        if (own) {
            set(11, Ui.button(Items.LIME_CONCRETE, "Снять с продажи", Formatting.GREEN,
                    Ui.t("Предмет вернётся в инвентарь", Formatting.GRAY)), (p, b, t) -> {
                Market.cancel(p, id);
                go(p, new ShopGui(p));
            });
        } else {
            set(11, Ui.button(Items.LIME_CONCRETE, "Подтвердить покупку", Formatting.GREEN,
                    Ui.t("Вы действительно хотите купить", Formatting.GRAY),
                    Ui.t(name, Formatting.WHITE),
                    Ui.t("за " + Ui.money(l.price) + "?", Formatting.GOLD)), (p, b, t) -> {
                Market.buy(p, id);
                go(p, new ShopGui(p));
            });
        }
        set(15, Ui.button(Items.RED_CONCRETE, "Отмена", Formatting.RED),
                (p, b, t) -> go(p, new ShopGui(p)));
        fillEmpty();
    }
}
