package com.pozdro.nuclearindustry.integration;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.AbsorberRecipe;
import com.pozdro.nuclearindustry.recipe.ElechamberRecipe;
import com.pozdro.nuclearindustry.recipe.GasOvenRecipe;
import com.pozdro.nuclearindustry.recipe.StillRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEINuclearIndustryPlugin implements IModPlugin {
    public static RecipeType<ElechamberRecipe> ELECTROLYSIS =
            new RecipeType<>(ElechamberRecipeCategory.UID, ElechamberRecipe.class);

    public static RecipeType<StillRecipe> DISTILATION =
            new RecipeType<>(StillRecipeCategory.UID, StillRecipe.class);

    public static RecipeType<GasOvenRecipe> GAS_OVENING =
            new RecipeType<>(GasOvenRecipeCategory.UID, GasOvenRecipe.class);

    public static RecipeType<AbsorberRecipe> ABSORBTION =
            new RecipeType<>(AbsorberRecipeCategory.UID, AbsorberRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(NuclearIndustry.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                ElechamberRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                StillRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                GasOvenRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                AbsorberRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<ElechamberRecipe> recipesElectrolysis = rm.getAllRecipesFor(ElechamberRecipe.Type.INSTANCE);
        registration.addRecipes(ELECTROLYSIS, recipesElectrolysis);

        List<StillRecipe> recipesDistillation = rm.getAllRecipesFor(StillRecipe.Type.INSTANCE);
        registration.addRecipes(DISTILATION, recipesDistillation);

        List<GasOvenRecipe> recipesGasOven = rm.getAllRecipesFor(GasOvenRecipe.Type.INSTANCE);
        registration.addRecipes(GAS_OVENING, recipesGasOven);

        List<AbsorberRecipe> recipesAbsorber = rm.getAllRecipesFor(AbsorberRecipe.Type.INSTANCE);
        registration.addRecipes(ABSORBTION,recipesAbsorber );
    }

}
