package io.github.kydzombie.runebound.mixin;

import io.github.kydzombie.runebound.block.entity.InfusionPedestalBlockEntity;
import io.github.kydzombie.runebound.recipe.InfusionPedestalRecipeRegistry;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResultType;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawContext {
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "render(FZII)V", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addInfusionMessage(float f, boolean bl, int i, int j, CallbackInfo ci, class_564 display, int screenWidth, int screenHeight, TextRenderer textRenderer) {
        var catalyst = minecraft.player.getHand();
        if (catalyst == null || minecraft.field_2823 == null || minecraft.field_2823.type != HitResultType.BLOCK) return;

        int blockX = minecraft.field_2823.blockX;
        int blockY = minecraft.field_2823.blockY;
        int blockZ = minecraft.field_2823.blockZ;
        if (minecraft.world.getBlockEntity(blockX, blockY, blockZ) instanceof InfusionPedestalBlockEntity blockEntity) {
            if (blockEntity.getStack(0) != null) return;

            ItemStack[] inputs = blockEntity.catalyzers.toArray(ItemStack[]::new);
            ItemStack output = InfusionPedestalRecipeRegistry.getOutput(catalyst, inputs);
            if (output == null) return;

            GL11.glPushMatrix();
            GL11.glScalef(1.5f, 1.5f, 1.5f);
            drawCenteredTextWithShadow(minecraft.textRenderer,
                    "Will craft: " + output.getItem().getTranslatedName(),
                    screenWidth / 3, screenHeight / 4, 0xFFFFFF);
            GL11.glPopMatrix();
        }
    }
}
