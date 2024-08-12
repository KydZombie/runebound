package io.github.kydzombie.runebound.item.tool;

import net.minecraft.block.Block;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public class CrumblingAxeItem extends BasicAxeItem {
    public CrumblingAxeItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
    }

    private BlockState blockState = null;
    private int blockMeta = 0;

    @Override
    public boolean preMine(ItemStack stack, BlockState blockState, int x, int y, int z, int side, PlayerEntity player) {
        this.blockState = blockState;
        blockMeta = player.world.getBlockMeta(x, y, z);
        return super.preMine(stack, blockState, x, y, z, side, player);
    }

    @Override
    public boolean postMine(ItemStack stack, int blockId, int x, int y, int z, LivingEntity miner) {
        if (blockState == null || blockId != Block.LOG.id) return super.postMine(stack, blockId, x, y, z, miner);
        World world = miner.world;
        int currY = y + 1;
        ArrayList<BlockPos> logs = new ArrayList<>();
        while (world.getBlockState(x, currY, z) == blockState &&
                world.getBlockMeta(x, currY, z) == blockMeta) {
            logs.add(new BlockPos(x, currY, z));
            currY++;
        }
        for (BlockPos pos : logs) {
            // TODO: Make it actually work with non-oak logs
            // TODO: Make it work with branches
            world.setBlock(pos.x, pos.y, pos.z, 0);
            FallingBlockEntity entity = new FallingBlockEntity(world, (float)pos.x + 0.5F, (float)pos.y + 0.5F, (float)pos.z + 0.5F, blockId);
            world.spawnEntity(entity);
        }
        return super.postMine(stack, blockId, x, y, z, miner);
    }
}
