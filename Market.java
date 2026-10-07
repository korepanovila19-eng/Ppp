package com.example.shopmod;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public final class Market {
    public static final int MAX_LISTINGS = 36; // лотов на одного игрока

    public static void give(ServerPlayerEntity p, ItemStack st) {
        p.getInventory().insertStack(st);
        if (!st.isEmpty()) p.dropItem(st, false);
    }

    public static void buy(ServerPlayerEntity p, long id) {
        MinecraftServer s = p.getServer();
        ShopState st = ShopState.get(s);
        Listing l = st.find(id);
        if (l == null) {
            p.sendMessage(Ui.t("Этот лот уже продан или снят с продажи.", Formatting.RED));
            return;
        }
        if (l.seller.equals(p.getUuid())) {
            p.sendMessage(Ui.t("Это ваш собственный лот.", Formatting.RED));
            return;
        }
        if (st.balance(p.getUuid()) < l.price) {
            p.sendMessage(Ui.t("Недостаточно денег. Нужно " + Ui.money(l.price)
                    + ", у вас " + Ui.money(st.balance(p.getUuid())), Formatting.RED));
            return;
        }
        st.listings.remove(l);
        st.markDirty();
        Economy.add(s, p.getUuid(), -l.price);
        Economy.add(s, l.seller, l.price);
        give(p, l.stack.copy());
        String name = l.stack.getName().getString() + " x" + l.stack.getCount();
        p.sendMessage(Ui.t("Куплено: " + name + " за " + Ui.money(l.price), Formatting.GREEN));
        ServerPlayerEntity seller = s.getPlayerManager().getPlayer(l.seller);
        if (seller != null) {
            seller.sendMessage(Ui.t(p.getName().getString() + " купил " + name + " за "
                    + Ui.money(l.price), Formatting.GOLD));
        }
    }

    public static void cancel(ServerPlayerEntity p, long id) {
        ShopState st = ShopState.get(p.getServer());
        Listing l = st.find(id);
        if (l == null || !l.seller.equals(p.getUuid())) {
            p.sendMessage(Ui.t("Лот не найден.", Formatting.RED));
            return;
        }
        st.listings.remove(l);
        st.markDirty();
        give(p, l.stack.copy());
        p.sendMessage(Ui.t("Лот снят с продажи, предмет возвращён.", Formatting.YELLOW));
    }
}
