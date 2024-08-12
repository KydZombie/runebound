package io.github.kydzombie.runebound.block;

import io.github.kydzombie.runebound.block.entity.AltarBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class AltarBlock extends TemplateBlockWithEntity {
    private static final Random random = new Random();

    public AltarBlock(Identifier identifier) {
        super(identifier, Material.STONE);
        setTranslationKey(identifier);
        setHardness(2.5f);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new AltarBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof AltarBlockEntity altar) {
            if (altar.canPlayerUse(player)) {
                return altar.swapItem(player);
            }
        }
        return false;
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        AltarBlockEntity blockEntity = (AltarBlockEntity) world.getBlockEntity(x, y, z);

        ItemStack stack = blockEntity.getStack(0);
        if (stack == null) return;

        float var8 = random.nextFloat() * 0.8F + 0.1F;
        float var9 = random.nextFloat() * 0.8F + 0.1F;
        float var10 = random.nextFloat() * 0.8F + 0.1F;

        ItemEntity var12 = new ItemEntity(world, (float)x + var8, (float)y + var9, (float)z + var10, stack);
        float var13 = 0.05F;
        var12.velocityX = random.nextGaussian() * var13;
        var12.velocityY = random.nextGaussian() * var13 + 0.2F;
        var12.velocityZ = random.nextGaussian() * var13;
        world.spawnEntity(var12);
    }

    public static AltarBlockEntity[] getAllAltars(World world, int x, int y, int z) {
        if (!(world.getBlockState(x, y, z).getBlock() instanceof AltarBlock)) return new AltarBlockEntity[0];
        ArrayList<AltarBlockEntity> altars = new ArrayList<>();
        ArrayList<BlockPos> checked = new ArrayList<>();
        Stack<BlockPos> toCheck = new Stack<>();
        toCheck.push(new BlockPos(x, y, z));
        do {
            BlockPos pos = toCheck.pop();
            if (checked.contains(pos)) continue;
            if (world.getBlockState(pos).getBlock() instanceof AltarBlock) {
                altars.add((AltarBlockEntity) world.getBlockEntity(pos.x, pos.y, pos.z));
                toCheck.add(pos.add(1, 0, 0));
                toCheck.add(pos.add(-1, 0, 0));
                toCheck.add(pos.add(0, 0, 1));
                toCheck.add(pos.add(-1, 0, -1));
            }
            checked.add(pos);
        } while (!toCheck.isEmpty());

        return altars.toArray(AltarBlockEntity[]::new);
    }
}
