package io.github.kydzombie.runebound.item.tool;

import io.github.kydzombie.runebound.Runebound;
import io.github.kydzombie.runebound.custom.VeinMiningContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.Identifier;

public class VeinPickaxeItem extends BasicPickaxeItem {
    public static int veinMineCap = 50;

    public VeinPickaxeItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
    }

    @Override
    public boolean postMine(ItemStack stack, int blockId, int x, int y, int z, LivingEntity entity) {
        if (entity instanceof PlayerEntity player && !player.isSneaking()) {
            Runebound.veinMiningContexts.add(new VeinMiningContext(player, player.world, new BlockPos(x, y, z), blockId, 12));
            return true;
//            VeinMine(stack, blockId, x, y, z, player);
        }
        return super.postMine(stack, blockId, x, y, z, entity);
    }

//    private ArrayList<BlockPos> checkNearbyBlocks(World world, int blockId, BlockPos pos) {
//        ArrayList<BlockPos> foundBlocks = new ArrayList<>();
//        for (int x = pos.x - 1; x <= pos.x + 1; x++) {
//            for (int y = pos.y - 1; y <= pos.y + 1; y++) {
//                for (int z = pos.z - 1; z <= pos.z + 1; z++) {
//                    if (world.getBlockId(x, y, z) == blockId) {
//                        foundBlocks.add(new BlockPos(x, y, z));
//                    }
//                }
//            }
//        }
//
//        return foundBlocks;
//    }

//    private void VeinMine(ItemStack stack, int blockId, int x, int y, int z, LivingEntity player) {
//        World world = player.world;
//
//        ArrayList<BlockPos> blocksToCheck = new ArrayList<>();
//        ArrayList<BlockPos> blocksChecked = new ArrayList<>();
//        blocksToCheck.add(new BlockPos(x, y, z));
//
//        ArrayList<BlockPos> check;
//        while (!blocksToCheck.isEmpty() && blocksChecked.size() < veinMineCap) {
//
//            check = checkNearbyBlocks(world, blockId, blocksToCheck.get(0));
//
//            blocksChecked.add(blocksToCheck.get(0));
//            blocksToCheck.removeAll(blocksChecked);
//            check.removeAll(blocksChecked);
//
//            blocksToCheck.addAll(check);
//        }
//
//        int size = blocksChecked.size();
//
//        if (size > veinMineCap) {
//            blocksChecked.subList(veinMineCap, size).clear();
//        }
//
//        blocksChecked.remove(0);
//
//        blocksChecked.forEach((pos) -> {
//            Block block = Block.BLOCKS[world.getBlockId(pos.x, pos.y, pos.z)];
//            int meta = world.getBlockMeta(pos.x, pos.y, pos.z);
//            world.setBlock(pos.x, pos.y, pos.z, 0);
//            block.afterBreak(world, (PlayerEntity) player,  pos.x, pos.y, pos.z, meta);
//            stack.damage(1, player);
//        });
//
//    }
}
