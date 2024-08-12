package io.github.kydzombie.runebound;

import com.matthewperiut.accessoryapi.api.AccessoryRegister;
import io.github.kydzombie.runebound.block.AltarBlock;
import io.github.kydzombie.runebound.block.AmethystOreBlock;
import io.github.kydzombie.runebound.block.InfusionPedestalBlock;
import io.github.kydzombie.runebound.block.entity.AltarBlockEntity;
import io.github.kydzombie.runebound.block.entity.InfusionPedestalBlockEntity;
import io.github.kydzombie.runebound.custom.VeinMiningContext;
import io.github.kydzombie.runebound.item.BasicItem;
import io.github.kydzombie.runebound.item.GauntletItem;
import io.github.kydzombie.runebound.item.RuneItem;
import io.github.kydzombie.runebound.item.SpellGemItem;
import io.github.kydzombie.runebound.item.tool.*;
import io.github.kydzombie.runebound.recipe.InfusionRecipe;
import io.github.kydzombie.runebound.recipe.InfusionPedestalRecipeRegistry;
import net.fabricmc.api.ModInitializer;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.tick.GameTickEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Objects;

public class Runebound implements ModInitializer {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    // Items
    public static BasicItem amethyst;
    public static BasicItem featherBottle;
    public static BasicItem windBottle;
    public static BasicItem baseRune;

    // Runes
    public static RuneItem earthRune;
    public static RuneItem fireRune;
    public static RuneItem waterRune;
    public static RuneItem airRune;
    public static RuneItem timeRune;
    public static RuneItem sacrificeRune;

    // Tools & Equipment
    public static GauntletItem gauntlet;

    public static BasicPickaxeItem amethystPickaxe;
    public static HungryPickaxeItem hungryPickaxe;
    public static VeinPickaxeItem veinMiningPickaxe;
    public static BasicAxeItem amethystAxe;
    public static CrumblingAxeItem crumblingAxe;

    public static SpellGemItem spellGem;

    @EventListener
    private void registerItems(ItemRegistryEvent event) {
        LOGGER.info("Registering items...");
        amethyst = new BasicItem(NAMESPACE.id("amethyst"));
        featherBottle = new BasicItem(NAMESPACE.id("feather_bottle"));
        windBottle = new BasicItem(NAMESPACE.id("wind_bottle"));
        baseRune = new BasicItem(NAMESPACE.id("rune_base"));

        // Runes
        earthRune = new RuneItem(NAMESPACE.id("rune_earth"));
        fireRune = new RuneItem(NAMESPACE.id("rune_fire"));
        waterRune = new RuneItem(NAMESPACE.id("rune_water"));
        airRune = new RuneItem(NAMESPACE.id("rune_air"));
        timeRune = new RuneItem(NAMESPACE.id("rune_time"));
        sacrificeRune = new RuneItem(NAMESPACE.id("rune_sacrifice"));

        // Tools & Equipment
        gauntlet = new GauntletItem(NAMESPACE.id("gauntlet"));

        amethystPickaxe = new BasicPickaxeItem(NAMESPACE.id("amethyst_pickaxe"), ToolMaterial.DIAMOND);
        hungryPickaxe = new HungryPickaxeItem(NAMESPACE.id("hungry_pickaxe"), ToolMaterial.GOLD);
        veinMiningPickaxe = new VeinPickaxeItem(NAMESPACE.id("vein_pickaxe"), ToolMaterial.DIAMOND);
        amethystAxe = new BasicAxeItem(NAMESPACE.id("amethyst_axe"), ToolMaterial.DIAMOND);
        crumblingAxe = new CrumblingAxeItem(NAMESPACE.id("crumbling_axe"), ToolMaterial.DIAMOND);

        spellGem = new SpellGemItem(NAMESPACE.id("spell_gem"));

        // TODO: Move out of here
        InfusionPedestalRecipeRegistry.addRecipe(new InfusionRecipe(
                new ItemStack[]{ new ItemStack(windBottle), new ItemStack(Block.WOOL) },
                new ItemStack(baseRune),
                new ItemStack(airRune)
        ));
    }

    public static AmethystOreBlock amethystOre;
    public static InfusionPedestalBlock infusionPedestal;
    public static AltarBlock altar;

    @EventListener
    private void registerBlocks(BlockRegistryEvent event) {
        amethystOre = new AmethystOreBlock(NAMESPACE.id("amethyst_ore"));
        infusionPedestal = new InfusionPedestalBlock(NAMESPACE.id("infusion_pedestal"));
        altar = new AltarBlock(NAMESPACE.id("altar"));
    }

    @EventListener
    private void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(InfusionPedestalBlockEntity.class, NAMESPACE.id("infusion_pedestal").toString());
        event.register(AltarBlockEntity.class, NAMESPACE.id("altar").toString());
    }

    public static ArrayList<VeinMiningContext> veinMiningContexts = new ArrayList<>();


    @EventListener
    private void tickEvents(GameTickEvent.End event) {
        veinMiningContexts = new ArrayList<>(veinMiningContexts.stream().map(context -> {
            World world = context.world;
            if (world == null) {
                System.out.println("World is null!");
                veinMiningContexts.remove(context);
                return null;
            }
            ArrayList<BlockPos> foundBlocks = new ArrayList<>();
            for (BlockPos pos : context.toBeVeinMined) {
                System.out.println("x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ());
                for (int x = pos.x - 1; x <= pos.x + 1; x++) {
                    for (int y = pos.y - 1; y <= pos.y + 1; y++) {
                        for (int z = pos.z - 1; z <= pos.z + 1; z++) {
                            BlockState foundState = world.getBlockState(pos);
                            System.out.println("foundState = " + foundState);
                            int foundId = foundState.getBlock().id;
                            if (foundId == context.originalBlockId) {
                                BlockPos foundPos = new BlockPos(x, y, z);
                                System.out.println("Found at " + foundPos);
                                if (!foundBlocks.contains(foundPos)) {
                                    foundBlocks.add(foundPos);
                                }
                            }
                        }
                    }
                }
                System.out.println("Would break " + pos);
                world.setBlock(pos.x, pos.y, pos.z, 0);
            }

            if (foundBlocks.isEmpty()) {
                return null;
            } else {
                context.toBeVeinMined.clear();
                context.toBeVeinMined.addAll(foundBlocks);
                return context;
            }
        }).filter(Objects::nonNull).toList());
    }

    @Override
    public void onInitialize() {
        AccessoryRegister.add("gloves");
    }
}
