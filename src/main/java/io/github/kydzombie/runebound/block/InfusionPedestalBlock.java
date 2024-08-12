package io.github.kydzombie.runebound.block;

import io.github.kydzombie.runebound.block.entity.InfusionPedestalBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class InfusionPedestalBlock extends TemplateBlockWithEntity {
    private static final Random random = new Random();
    public InfusionPedestalBlock(Identifier identifier) {
        super(identifier, Material.STONE);
        setTranslationKey(identifier);
        setHardness(2.5f);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new InfusionPedestalBlockEntity();
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        InfusionPedestalBlockEntity blockEntity = ((InfusionPedestalBlockEntity) world.getBlockEntity(x, y, z));
        if (player.getHand() != null) {
            if (player.isSneaking()) {
                return blockEntity.setCatalystItem(player);
            } else {
                blockEntity.addItem(player.getHand().split(1));
                if (player.getHand().count == 0) {
                    player.inventory.setStack(player.inventory.selectedSlot, null);
                }
            }
        } else {
            ItemStack stack = blockEntity.removeItem();
            if (stack != null) {
                player.inventory.setStack(player.inventory.selectedSlot, stack);
            }
        }
        return true;
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        InfusionPedestalBlockEntity blockEntity = (InfusionPedestalBlockEntity) world.getBlockEntity(x, y, z);

        for(int i = 0; i < blockEntity.size(); ++i) {
            ItemStack stack = blockEntity.getStack(i);
            if (stack == null) continue;

            float var8 = random.nextFloat() * 0.8F + 0.1F;
            float var9 = random.nextFloat() * 0.8F + 0.1F;
            float var10 = random.nextFloat() * 0.8F + 0.1F;

            ItemEntity itemEntity = new ItemEntity(world, x + var8, y + var9, z + var10, stack);
            float var13 = 0.05F;
            itemEntity.velocityX = random.nextGaussian() * var13;
            itemEntity.velocityY = random.nextGaussian() * var13 + 0.2F;
            itemEntity.velocityZ = random.nextGaussian() * var13;
            world.spawnEntity(itemEntity);
        }
    }
}
