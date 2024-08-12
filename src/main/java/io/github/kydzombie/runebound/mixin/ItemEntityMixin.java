package io.github.kydzombie.runebound.mixin;

import io.github.kydzombie.runebound.Runebound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public ItemStack stack;

    public ItemEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "damage(I)V", at = @At("HEAD"), cancellable = true)
    private void cancelBurn(int amount, CallbackInfo ci) {
        // TODO: Make more difficult? Maybe needs fire upgraded by wind rune?
        if (stack.getItem() == Runebound.baseRune) {
            markDead();
            ItemEntity itemEntity = new ItemEntity(world, x, y, z, new ItemStack(Runebound.fireRune));
            float var13 = 0.05F;
            itemEntity.velocityX = (float)random.nextGaussian() * var13;
            itemEntity.velocityY = (float)random.nextGaussian() * var13 + 0.2F;
            itemEntity.velocityZ = (float)random.nextGaussian() * var13;
            world.spawnEntity(itemEntity);
            ci.cancel();
        } else if (stack.getItem() == Runebound.fireRune) {
            ci.cancel();
        }
    }
}
