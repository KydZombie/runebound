package io.github.kydzombie.runebound;

import io.github.kydzombie.runebound.block.entity.AltarBlockEntity;
import io.github.kydzombie.runebound.block.entity.InfusionPedestalBlockEntity;
import io.github.kydzombie.runebound.client.AltarBlockEntityRenderer;
import io.github.kydzombie.runebound.client.InfusionPedestalBlockEntityRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.event.block.entity.BlockEntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.client.event.render.model.ItemModelPredicateProviderRegistryEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.model.item.ItemModelPredicateProvider;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.jetbrains.annotations.Nullable;

public class RuneboundClient {
    @EventListener
    private void registerBlockEntityRenderers(BlockEntityRendererRegisterEvent event) {
        event.renderers.put(AltarBlockEntity.class, new AltarBlockEntityRenderer());
        event.renderers.put(InfusionPedestalBlockEntity.class, new InfusionPedestalBlockEntityRenderer());
    }

    @EventListener
    private void registerTextures(TextureRegisterEvent event) {
        // Items
        Runebound.amethyst.setTexture(Runebound.NAMESPACE.id("item/amethyst"));
        Runebound.featherBottle.setTexture(Runebound.NAMESPACE.id("item/feather_bottle"));
        Runebound.windBottle.setTexture(Runebound.NAMESPACE.id("item/wind_bottle"));
        Runebound.baseRune.setTexture(Runebound.NAMESPACE.id("item/rune/base"));

        // Runes
        Runebound.earthRune.setTexture(Runebound.NAMESPACE.id("item/rune/earth"));
        Runebound.fireRune.setTexture(Runebound.NAMESPACE.id("item/rune/fire"));
        Runebound.waterRune.setTexture(Runebound.NAMESPACE.id("item/rune/water"));
        Runebound.airRune.setTexture(Runebound.NAMESPACE.id("item/rune/air"));
        Runebound.timeRune.setTexture(Runebound.NAMESPACE.id("item/rune/time"));
        Runebound.sacrificeRune.setTexture(Runebound.NAMESPACE.id("item/rune/sacrifice"));

        // Tools & Equipment
        Runebound.gauntlet.setTexture(Runebound.NAMESPACE.id("item/gauntlet"));

        Runebound.amethystPickaxe.setTexture(Runebound.NAMESPACE.id("item/amethyst_pickaxe"));
        Runebound.hungryPickaxe.setTexture(Runebound.NAMESPACE.id("item/hungry_pickaxe"));
        Runebound.veinMiningPickaxe.setTexture(Runebound.NAMESPACE.id("item/vein_pickaxe"));
        Runebound.amethystAxe.setTexture(Runebound.NAMESPACE.id("item/amethyst_axe"));
        Runebound.crumblingAxe.setTexture(Runebound.NAMESPACE.id("item/crumbling_axe"));

        // Blocks
        var terrain = Atlases.getTerrain();
        Runebound.amethystOre.textureId = terrain.addTexture(Runebound.NAMESPACE.id("block/amethyst_ore")).index;
    }

    @EventListener
    private void registerPredicates(ItemModelPredicateProviderRegistryEvent event) {
        event.registry.register(Runebound.crumblingAxe, Runebound.NAMESPACE.id("should_animate"), (stack, world, entity, seed) -> {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            boolean thirdPerson = ((Minecraft)FabricLoader.getInstance().getGameInstance()).options.thirdPerson;
            if (entity == player) {
                if (thirdPerson) {
                    return 0f;
                } else {
                    return 1f;
                }
            } else if (entity == null) {
                return 1f;
            } else {
                return 0f;
            }
        });
    }
}
