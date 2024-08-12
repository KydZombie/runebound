package io.github.kydzombie.runebound.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class AltarBlockEntity extends BlockEntity implements Inventory {
    private ItemStack itemStack;

    @Environment(EnvType.CLIENT) public ItemEntity renderedItem = null;

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot != 0) return null;
        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot != 0) return null;
        ItemStack returnStack;
        if (itemStack.count < amount) {
            returnStack = itemStack;
            itemStack = null;
        } else {
            returnStack = itemStack.split(amount);
        }
        markDirty();
        return returnStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        itemStack = stack;
        if (itemStack != null && itemStack.count > getMaxCountPerStack()) {
            itemStack.count = getMaxCountPerStack();
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "Altar";
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return !(player.getSquaredDistance((double)x + 0.5, (double)y + 0.5, (double)z + 0.5) > 64.0);
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            renderedItem = null;
        }
    }

    public boolean swapItem(PlayerEntity player) {
        ItemStack hand = player.getHand();
        if (itemStack != null) {
            if (hand == null) {
                player.inventory.setStack(player.inventory.selectedSlot, itemStack);
                itemStack = null;
                player.inventory.markDirty();
                markDirty();
                return true;
            }
            // Check if hand is the same item
            if (hand.isItemEqual(itemStack) && hand.count < hand.getMaxCount()) {
                hand.count += itemStack.count;
                itemStack = null;
                player.inventory.markDirty();
                markDirty();
                return true;
            } else {
                return false;
            }
        } else if (hand != null) {
            itemStack = hand.split(1);
            player.inventory.markDirty();
            markDirty();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("Item")) {
            NbtCompound itemNbt = nbt.getCompound("Item");
            itemStack = new ItemStack(itemNbt);
        } else {
            itemStack = null;
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (itemStack == null) return;

        NbtCompound itemNbt = new NbtCompound();
        itemStack.writeNbt(itemNbt);

        nbt.put("Item", itemNbt);
    }
}
