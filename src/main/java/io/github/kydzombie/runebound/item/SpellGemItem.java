package io.github.kydzombie.runebound.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class SpellGemItem extends TemplateItem {
    private static final Random RANDOM = new Random();
    public SpellGemItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        world.createExplosion(null, x, y, z, RANDOM.nextFloat(1.0f, 3.0f));
        return super.useOnBlock(stack, user, world, x, y, z, side);
    }
}
