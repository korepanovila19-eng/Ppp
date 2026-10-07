package com.example.shopmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class Commands {
    public static void register(CommandDispatcher<ServerCommandSource> d) {
        d.register(CommandManager.literal("shop").executes(c -> {
            ServerPlayerEntity p = c.getSource().getPlayerOrThrow();
            new ShopGui(p).open(p);
            return 1;
        }));

        d.register(CommandManager.literal("sellitems").executes(c -> {
            ServerPlayerEntity p = c.getSource().getPlayerOrThrow();
            new SellItemsGui(p).open(p);
            return 1;
        }));

        d.register(CommandManager.literal("balance").executes(c -> {
            ServerPlayerEntity p = c.getSource().getPlayerOrThrow();
            p.sendMessage(Ui.t("Ваш баланс: " + Ui.money(Economy.get(p)), Formatting.GOLD));
            return 1;
        }));

        // /money give <игрок> <сумма>  (только операторы / с читами)
        d.register(CommandManager.literal("money")
                .requires(s -> s.hasPermissionLevel(2))
                .then(CommandManager.literal("give")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("amount", LongArgumentType.longArg())
                                        .executes(c -> {
                                            ServerPlayerEntity t = EntityArgumentType.getPlayer(c, "player");
                                            long a = LongArgumentType.getLong(c, "amount");
                                            Economy.add(c.getSource().getServer(), t.getUuid(), a);
                                            c.getSource().sendFeedback(() -> Text.literal(
                                                    "Выдано " + Ui.money(a) + " игроку " + t.getName().getString()), true);
                                            return 1;
                                        })))));
    }
}
