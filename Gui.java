package com.example.shopmod;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public abstract class Gui {
    public interface Click {
        void on(ServerPlayerEntity p, int button, SlotActionType type);
    }

    public final int rows;
    public final SimpleInventory inv;
    private final Text title;
    private final Map<Integer, Click> actions = new HashMap<>();

    protected Gui(String title, int rows) {
        this.rows = rows;
        this.title = Text.literal(title);
        this.inv = new SimpleInventory(rows * 9);
    }

    protected abstract void render();

    protected final void begin() {
        for (int i = 0; i < inv.size(); i++) inv.setStack(i, ItemStack.EMPTY);
        actions.clear();
    }

    protected final void set(int slot, ItemStack st) {
        inv.setStack(slot, st);
        actions.remove(slot);
    }

    protected final void set(int slot, ItemStack st, Click c) {
        inv.setStack(slot, st);
        actions.put(slot, c);
    }

    protected final void fillRange(int from, int to) {
        for (int i = from; i < to; i++) set(i, Ui.filler());
    }

    protected final void fillEmpty() {
        for (int i = 0; i < inv.size(); i++) if (inv.getStack(i).isEmpty()) inv.setStack(i, Ui.filler());
    }

    public final void click(ServerPlayerEntity p, int slot, int button, SlotActionType type) {
        Click c = actions.get(slot);
        if (c != null) c.on(p, button, type);
    }

    public final void refresh() {
        render();
    }

    public final void open(ServerPlayerEntity p) {
        render();
        p.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, playerInv, player) -> new GuiHandler(syncId, playerInv, this), title));
    }

    /** Открыть другое меню (в конце тика). */
    protected static void go(ServerPlayerEntity p, Gui g) {
        Scheduler.later(() -> g.open(p));
    }
}
