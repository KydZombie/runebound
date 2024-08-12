package io.github.kydzombie.runebound.client;

import io.github.kydzombie.runebound.block.entity.InfusionPedestalBlockEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.ItemEntity;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class InfusionPedestalBlockEntityRenderer extends BlockEntityRenderer {
    @SuppressWarnings("deprecation")
    private static final Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();

    @Override
    public void render(BlockEntity blockEntity, double x, double y, double z, float f) {
        if (blockEntity instanceof InfusionPedestalBlockEntity infusionPedestal) {
            GL11.glPushMatrix();
            List<ItemEntity> catalyzerEntities = infusionPedestal.getCatalyzerEntities();
            for (int i = 0; i < catalyzerEntities.size(); i++) {
                ItemEntity itemEntity = catalyzerEntities.get(i);
                double transX = itemEntity.x - infusionPedestal.x;
                double transY = itemEntity.y - infusionPedestal.y;
                double transZ = itemEntity.z - infusionPedestal.z;
                GL11.glTranslated(x + transX, y + transY, z + transZ);
                EntityRenderDispatcher.field_2489.method_1920(itemEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                GL11.glTranslated(-(x + transX), -(y + transY), -(z + transZ));
            }

            ItemEntity catalystEntity = infusionPedestal.getCatalystEntity();
            if (catalystEntity != null) {
                double transX = catalystEntity.x - infusionPedestal.x;
                double transY = catalystEntity.y - infusionPedestal.y;
                double transZ = catalystEntity.z - infusionPedestal.z;
                GL11.glTranslated(x + transX, y + transY, z + transZ);
                EntityRenderDispatcher.field_2489.method_1920(catalystEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                GL11.glTranslated(-(x + transX), -(y + transY), -(z + transZ));
            }
            GL11.glPopMatrix();
        }
    }
}
