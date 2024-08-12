package io.github.kydzombie.runebound.recipe;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class InfusionPedestalRecipeRegistry {
    private static final ArrayList<InfusionRecipe> recipes = new ArrayList<>();

    public static void addRecipe(final InfusionRecipe recipe) {
        recipes.add(recipe);
    }

    private static final Comparator<ItemStack> comparator = Comparator
            .comparingInt((ItemStack stack) -> stack.itemId)
            .thenComparingInt((ItemStack stack) -> stack.count);

    public static @Nullable ItemStack getOutput(ItemStack catalyst, ItemStack[] inputs) {
        if (catalyst == null) return null;
        ItemStack[] cleanInput = Arrays.stream(inputs).filter(Objects::nonNull).sorted(comparator).toArray(ItemStack[]::new);
        for (final InfusionRecipe recipe : recipes) {
            ItemStack[] recipeInputs = Arrays.stream(recipe.getInputs()).sorted(comparator).toArray(ItemStack[]::new);
            if (!recipe.getCatalyst().isItemEqual(catalyst)) continue;
            if (recipeInputs.length != cleanInput.length) continue;
            boolean valid = true;
            for (int i = 0; i < recipeInputs.length; i++) {
                if (!recipeInputs[i].isItemEqual(cleanInput[i])) {
                    valid = false;
                    break;
                }
            }
            if (valid) return recipe.getOutput();
        }
        return null;
    }
}
