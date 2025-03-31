package com.pozdro.nuclearindustry.integration;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.recipe.GasOvenRecipe;
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

public class GasOvenRecipeCategory implements IRecipeCategory<GasOvenRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(NuclearIndustry.MODID, "gasoven");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(NuclearIndustry.MODID, "textures/gui/gasoven_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public GasOvenRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE,4,4,169,78);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModBlocks.GAS_OVEN.get()));
    }

    @Override
    public RecipeType<GasOvenRecipe> getRecipeType() {
        return JEINuclearIndustryPlugin.GAS_OVENING;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Gas Oven");
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
    public void setRecipe(IRecipeLayoutBuilder builder, GasOvenRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,8-4,72-4); //0
        builder.addSlot(RecipeIngredientRole.OUTPUT,28-4,72-4);//1
        builder.addSlot(RecipeIngredientRole.INPUT,56-4,53-4).addIngredients(recipe.getIngredients().get(0));//2
        builder.addSlot(RecipeIngredientRole.OUTPUT,116-4,53-4).addItemStack(recipe.getResultItem());//3

        builder.addSlot(RecipeIngredientRole.INPUT,28-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackIN()))
                .setFluidRenderer(64000,false,14,63);

        builder.addSlot(RecipeIngredientRole.INPUT,10-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackIN2()))
                .setFluidRenderer(64000,false,14,63);

        builder.addSlot(RecipeIngredientRole.OUTPUT,138-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackOUT()))
                .setFluidRenderer(64000,false,14,63);
    }

}
