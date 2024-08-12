package io.github.kydzombie.runebound.recipe;

import net.minecraft.item.ItemStack;

public class InfusionRecipe {
    private final ItemStack catalyst;
    private final ItemStack[] inputs;
    private final ItemStack output;

    public InfusionRecipe(ItemStack[] inputs, ItemStack catalyst, ItemStack output) {
        this.catalyst = catalyst;
        this.inputs = inputs;
        this.output = output;
    }

    public ItemStack getCatalyst() {
        return catalyst;
    }

    public ItemStack[] getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }
}
