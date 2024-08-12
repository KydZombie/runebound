package io.github.kydzombie.runebound.block;

import io.github.kydzombie.runebound.Runebound;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AmethystOreBlock extends TemplateBlock {
    public AmethystOreBlock(Identifier identifier) {
        super(identifier, Material.STONE);
        setTranslationKey(identifier);
        setHardness(5f);
    }

    @Override
    public List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta) {
        return new ArrayList<>(){{add(new ItemStack(Runebound.amethyst));}};
    }
}
