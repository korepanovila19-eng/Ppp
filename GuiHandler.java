package com.example.shopmod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

/** Обычный сундук для ванильного клиента, но все клики перехватываются и ничего нельзя вынуть. */
public class GuiHandler extends GenericContainerScreenHandler {
    private final Gui gui;

    public GuiHandler(int syncId, PlayerInventory playerInventory, Gui gui) {
        super(typeFor(gui.rows), syncId, playerInventory, gui.inv, gui.rows);
        this.gui = gui;
    }

    private static ScreenHandlerType<?> typeFor(int rows) {
        return switch (rows) {
            case 1 -> ScreenHandlerType.GENERIC_9X1;
            case 2 -> ScreenHandlerType.GENERIC_9X2;
            case 3 -> ScreenHandlerType.GENERIC_9X3;
            case 4 -> ScreenHandlerType.GENERIC_9X4;
            case 5 -> ScreenHandlerType.GENERIC_9X5;
            default -> ScreenHandlerType.GENERIC_9X6;
        };
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex >= 0 && slotIndex < gui.rows * 9 && player instanceof ServerPlayerEntity sp) {
            gui.click(sp, slotIndex, button, actionType);
        }
        // всё остальное игнорируем: сервер сам пересинхронизирует клиента
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return false;
    }
}
