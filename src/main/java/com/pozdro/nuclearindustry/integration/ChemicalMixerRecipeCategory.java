package com.pozdro.nuclearindustry.integration;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.recipe.ChemicalMixerRecipe;
import com.pozdro.nuclearindustry.recipe.StillRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ChemicalMixerRecipeCategory implements IRecipeCategory<ChemicalMixerRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(NuclearIndustry.MODID, "chemical_mixing");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(NuclearIndustry.MODID, "textures/gui/chemical_mixer_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ChemicalMixerRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE,4,4,169,78);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModBlocks.CHEMICALMIXER.get()));
    }

    @Override
    public RecipeType<ChemicalMixerRecipe> getRecipeType() {
        return JEINuclearIndustryPlugin.CHEMICAL_MIXING;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Chemical Mixer");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChemicalMixerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,61-4,17-4); //0
        builder.addSlot(RecipeIngredientRole.INPUT,61-4,36-4).addIngredients(recipe.getIngredients().get(0));//1
        builder.addSlot(RecipeIngredientRole.INPUT,61-4,55-4).addIngredients(recipe.getIngredients().get(0));//2
        builder.addSlot(RecipeIngredientRole.OUTPUT,135-4,55-4).addIngredients(recipe.getIngredients().get(0));//3
        builder.addSlot(RecipeIngredientRole.OUTPUT,135-4,36-4).addItemStack(recipe.getResultItem());//4


        builder.addSlot(RecipeIngredientRole.INPUT,9-4,8-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackIN()))
                .setFluidRenderer(64000,false,14,63);

        builder.addSlot(RecipeIngredientRole.INPUT,26-4,8-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackIN1()))
                .setFluidRenderer(64000,false,14,63);

        /*
        builder.addSlot(RecipeIngredientRole.INPUT,43-4,8-4).addIngredients(ForgeTypes.FLUID_STACK,
                    List.of(recipe.getFluidStackIN()))
            .setFluidRenderer(64000,false,14,63);

        */


        builder.addSlot(RecipeIngredientRole.OUTPUT,116-4,8-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackOUT()))
                .setFluidRenderer(64000,false,14,63);



    }
}
