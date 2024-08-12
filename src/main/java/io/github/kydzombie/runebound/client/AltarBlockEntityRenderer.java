package io.github.kydzombie.runebound.client;

import io.github.kydzombie.runebound.block.entity.AltarBlockEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class AltarBlockEntityRenderer extends BlockEntityRenderer {
    @SuppressWarnings("deprecation")
    private static final Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();

    @Override
    public void render(BlockEntity blockEntity, double x, double y, double z, float f) {
        if (blockEntity instanceof AltarBlockEntity altar) {
            ItemStack stack = altar.getStack(0);
            if (stack == null) return;

            if (altar.renderedItem == null) {
                PlayerEntity player = minecraft.player;
                altar.renderedItem = new ItemEntity(player.world, altar.x, altar.y + 1, altar.z, stack);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.5F, (float)y+1.0F, (float)z + 0.5F);
            EntityRenderDispatcher.field_2489.method_1920(altar.renderedItem, 0.0, 0.0, 0.0, 0.0F, 0.0F);
            GL11.glPopMatrix();
        }
    }
}
