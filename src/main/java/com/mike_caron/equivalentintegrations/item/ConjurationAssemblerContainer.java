package com.mike_caron.equivalentintegrations.item;

import com.mike_caron.equivalentintegrations.inventory.GhostSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ConjurationAssemblerContainer
        extends Container
{
    private final IInventory playerInventory;
    private final IInventory containerInventory;
    private Slot filterSlot;

    private final int protectedSlot;
    private final int protectedIndex;

    public static final int GUI_ID = 3;

    public ConjurationAssemblerContainer(IInventory playerInventory, IInventory containerInventory, int protectedIndex)
    {
        this.playerInventory = playerInventory;
        this.containerInventory = containerInventory;

        addOwnSlots();
        addPlayerSlots(playerInventory);

        this.protectedIndex = protectedIndex;
        this.protectedSlot = findSlotForIndex(protectedIndex);
    }

    private int findSlotForIndex(int index)
    {
        for(Slot slot : inventorySlots)
        {
            if(slot.getSlotIndex() == protectedIndex && slot.inventory == playerInventory)
                return slot.slotNumber;
        }

        return -1;
    }

    private void addPlayerSlots(IInventory playerInventory)
    {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 11 + col * 18;
                int y = row * 18 + 71;
                this.addSlotToContainer(new Slot(playerInventory, (row + 1) * 9 + col, x, y));
            }
        }

        // Slots for the hotbar
        for (int col = 0; col < 9; ++col) {
            int x = 11 + col * 18;
            int y = 59 + 70;
            this.addSlotToContainer(new Slot(playerInventory, col, x, y) {
                @Override
                public boolean canTakeStack(EntityPlayer playerIn)
                {
                    return slotNumber != protectedSlot && super.canTakeStack(playerIn);
                }

                @Override
                public boolean isEnabled()
                {
                    return slotNumber != protectedSlot && super.isEnabled();
                }
            });
        }
    }

    private void addOwnSlots()
    {
        filterSlot = addSlotToContainer(new GhostSlot(this.containerInventory, 0, 83, 32));

    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(index == protectedSlot)
            return slot.getStack();

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 1)
            { //transferring from block -> player
                slot.putStack(ItemStack.EMPTY);
                return ItemStack.EMPTY;
            }
            else
            {
                this.filterSlot.putStack(itemstack);
                return itemstack;
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player)
    {
        if(slotId == protectedSlot)
            return ItemStack.EMPTY;
        if(clickType == ClickType.SWAP && dragType == protectedSlot)
            return ItemStack.EMPTY;

        return super.slotClick(slotId, dragType, clickType, player);
    }
}
