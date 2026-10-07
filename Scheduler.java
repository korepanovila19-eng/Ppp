package com.example.shopmod;

import java.util.ArrayDeque;

/** Выполняет действия в конце тика (безопасно открывать меню из клика). */
public final class Scheduler {
    private static final ArrayDeque<Runnable> QUEUE = new ArrayDeque<>();

    public static void later(Runnable r) {
        QUEUE.add(r);
    }

    public static void tick() {
        int n = QUEUE.size();
        for (int i = 0; i < n; i++) {
            Runnable r = QUEUE.poll();
            if (r == null) break;
            try {
                r.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
