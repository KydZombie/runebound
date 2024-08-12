package io.github.kydzombie.runebound.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class VeinMiningContext {
    public final PlayerEntity player;
    public final World world;
    public final int max;
    public final int originalBlockId;
    public ArrayList<BlockPos> toBeVeinMined = new ArrayList<>();
    public VeinMiningContext(PlayerEntity player, World world, BlockPos initialPos, int originalBlockId, int max) {
        this.player = player;
        this.world = world;
        this.originalBlockId = originalBlockId;
        this.max = max;
        toBeVeinMined.add(initialPos);
    }
}
