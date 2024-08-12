package io.github.kydzombie.runebound.block.entity;

import io.github.kydzombie.runebound.recipe.InfusionPedestalRecipeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Stack;

public class InfusionPedestalBlockEntity extends BlockEntity implements Inventory {
    private ItemStack catalyst = null;
    public final Stack<ItemStack> catalyzers = new Stack<>();

    @Environment(EnvType.CLIENT) private ItemEntity catalystEntity;
    @Environment(EnvType.CLIENT) private Stack<ItemEntity> catalyzerEntities = new Stack<>();

    private final int INFUSION_TIME = 30;
    private int infusionProgress = 0;

    @Override
    public int size() {
        return catalyzers.size() + 1;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot == 0) {
            return catalyst;
        } else {
            return catalyzers.get(slot - 1);
        }
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (getStack(slot) != null) {
            ItemStack stack;
            if (getStack(slot).count <= amount) {
                stack = getStack(slot);
                if (slot == 0) {
                    catalyst = null;
                } else {
                    catalyzers.remove(slot - 1);
                }
            } else {
                stack = getStack(slot).split(amount);
                if (getStack(0).count == 0) {
                    if (slot == 0) {
                        catalyst = null;
                    } else {
                        catalyzers.remove(slot - 1);
                    }
                }
            }
            markDirty();
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 0) {
            catalyst = stack;
        } else {
            catalyzers.set(slot - 1, stack);
        }
        if (stack != null && stack.count > getMaxCountPerStack()) {
            stack.count = getMaxCountPerStack();
        }

        markDirty();
    }

    @Override
    public String getName() {
        return "Infusion Pedestal";
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

        infusionProgress = 0;

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            catalystEntity = null;
            catalyzerEntities.clear();
        }
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("Catalyst")) {
            NbtCompound catalystNbt = nbt.getCompound("Catalyst");
            catalyst = new ItemStack(catalystNbt);
        }

        NbtList itemList = nbt.getList("Items");
        catalyzers.clear();

        catalyzers.setSize(itemList.size());

        for(int i = 0; i < itemList.size(); ++i) {
            NbtCompound itemNbt = (NbtCompound)itemList.get(i);
            int slot = itemNbt.getByte("Slot") & 255;
            catalyzers.set(slot, new ItemStack(itemNbt));
        }
    }

    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (catalyst != null) {
            NbtCompound catalystNbt = new NbtCompound();
            catalyst.writeNbt(catalystNbt);

            nbt.put("Catalyst", catalystNbt);
        }

        NbtList itemList = new NbtList();

        for(int i = 0; i < catalyzers.size(); ++i) {
            if (catalyzers.get(i) != null) {
                NbtCompound itemNbt = new NbtCompound();
                itemNbt.putByte("Slot", (byte)i);
                catalyzers.get(i).writeNbt(itemNbt);
                itemList.add(itemNbt);
            }
        }

        nbt.put("Items", itemList);
    }

    public void addItem(ItemStack stack) {
        catalyzers.push(stack);
        markDirty();
    }

    public @Nullable ItemStack removeItem() {
        if (catalyst != null) {
            ItemStack stack = catalyst;
            catalyst = null;
            markDirty();
            return stack;
        } else if (!catalyzers.isEmpty()) {
            ItemStack stack = catalyzers.pop();
            markDirty();
            return stack;
        } else {
            return null;
        }
    }

    public void craft() {
        ItemStack output = InfusionPedestalRecipeRegistry.getOutput(catalyst, catalyzers.toArray(ItemStack[]::new));
        if (output == null) return;
        catalyst = null;
        catalyzers.clear();
        markDirty();

        // dropStack
        float var6 = 0.7F;
        double var7 = (double)(world.random.nextFloat() * var6) + (double)(1.0F - var6) * 0.5;
        double var9 = (double)(world.random.nextFloat() * var6) + (double)(1.0F - var6) * 0.5;
        double var11 = (double)(world.random.nextFloat() * var6) + (double)(1.0F - var6) * 0.5;
        ItemEntity var13 = new ItemEntity(world, (double)x + var7, (double)y + 0.5 + var9, (double)z + var11, output.copy());
        var13.pickupDelay = 10;
        world.spawnEntity(var13);
    }

    public boolean setCatalystItem(PlayerEntity player) {
        if (catalyst == null) {
            catalyst = player.getHand().split(1);
            if (player.getHand().count == 0) {
                player.inventory.setStack(player.inventory.selectedSlot, null);
            }
            return true;
        } else {
            return false;
        }
    }

    @Environment(EnvType.CLIENT)
    public List<ItemEntity> getCatalyzerEntities() {
        if (catalyzerEntities.isEmpty() && !catalyzers.isEmpty()) {
            double circumference = 2 * Math.PI;
            double circleOffset = circumference / catalyzers.size();
            for (int i = 0; i < catalyzers.size(); i++) {
                double offset = i * circleOffset;
                double itemX = Math.sin(offset);
                double itemZ = Math.cos(offset);

                catalyzerEntities.push(new ItemEntity(world, x + 0.5 + itemX, y + 1.5, z + 0.5 + itemZ, catalyzers.get(i)));
            }
        }
        return catalyzerEntities;
    }

    @Environment(EnvType.CLIENT)
    public ItemEntity getCatalystEntity() {
        if (catalystEntity == null && catalyst != null) {
            catalystEntity = new ItemEntity(world, x + 0.5, y + 1, z + 0.5, catalyst);
        }
        return catalystEntity;
    }

    @Override
    public void tick() {
        ItemStack output = InfusionPedestalRecipeRegistry.getOutput(catalyst, catalyzers.toArray(ItemStack[]::new));
        if (output == null) {
            infusionProgress = 0;
            return;
        }

        if (infusionProgress >= INFUSION_TIME) {
            infusionProgress = 0;
            craft();
        } else {
            infusionProgress++;
            world.addParticle("smoke", x + 0.5, y + 1.25, z + 0.5, 0.0, 0.0, 0.0);
        }
    }
}
