package com.pozdro.nuclearindustry.integration;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.*;
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

    public static RecipeType<ChemicalMixerRecipe> CHEMICAL_MIXING =
            new RecipeType<>(ChemicalMixerRecipeCategory.UID, ChemicalMixerRecipe.class);

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
                ChemicalMixerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

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

        List<ChemicalMixerRecipe> recipesChemicalMixing = rm.getAllRecipesFor(ChemicalMixerRecipe.Type.INSTANCE);
        registration.addRecipes(CHEMICAL_MIXING, recipesChemicalMixing);

        List<GasOvenRecipe> recipesGasOven = rm.getAllRecipesFor(GasOvenRecipe.Type.INSTANCE);
        registration.addRecipes(GAS_OVENING, recipesGasOven);

        List<AbsorberRecipe> recipesAbsorber = rm.getAllRecipesFor(AbsorberRecipe.Type.INSTANCE);
        registration.addRecipes(ABSORBTION,recipesAbsorber );
    }

}
