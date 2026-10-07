package com.example.shopmod;

import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Баланс справа на экране: личная боковая панель (sidebar), отправляется пакетами
 * только этому игроку. Клиентский мод не нужен.
 */
public final class Hud {
    private static final String NAME = "shopmod_bal";
    private static final String ENTRY = "\u00a7aБаланс \u00a7e$";
    private static final Scoreboard BOARD = new Scoreboard();
    private static final ScoreboardObjective OBJ = BOARD.addObjective(
            NAME, ScoreboardCriterion.DUMMY,
            Text.literal("Кошелёк").formatted(Formatting.GOLD, Formatting.BOLD),
            ScoreboardCriterion.RenderType.INTEGER);

    public static void init(ServerPlayerEntity p) {
        p.networkHandler.sendPacket(new ScoreboardObjectiveUpdateS2CPacket(OBJ, 0)); // 0 = создать
        p.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(1, OBJ));          // 1 = sidebar
        update(p);
    }

    public static void update(ServerPlayerEntity p) {
        long bal = Economy.get(p);
        int score = (int) Math.min(bal, Integer.MAX_VALUE);
        p.networkHandler.sendPacket(new ScoreboardPlayerUpdateS2CPacket(
                ServerScoreboard.UpdateMode.CHANGE, NAME, ENTRY, score));
    }
}
