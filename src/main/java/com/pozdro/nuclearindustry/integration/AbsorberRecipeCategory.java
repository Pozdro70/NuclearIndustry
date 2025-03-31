package com.pozdro.nuclearindustry.integration;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.recipe.AbsorberRecipe;
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

public class AbsorberRecipeCategory implements IRecipeCategory<AbsorberRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(NuclearIndustry.MODID, "absorber");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(NuclearIndustry.MODID, "textures/gui/absorber_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public AbsorberRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE,4,4,169,78);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModBlocks.ABSORBER.get()));
    }

    @Override
    public RecipeType<AbsorberRecipe> getRecipeType() {
        return JEINuclearIndustryPlugin.ABSORBTION;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Absorber");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AbsorberRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,7-4,6-4); //0
        builder.addSlot(RecipeIngredientRole.INPUT,63-4,6-4);//1
        builder.addSlot(RecipeIngredientRole.OUTPUT,63-4,6-4);//1
        builder.addSlot(RecipeIngredientRole.OUTPUT,116-4,53-4).addItemStack(recipe.getResultItem());//2

        builder.addSlot(RecipeIngredientRole.INPUT,28-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackIN()))
                .setFluidRenderer(64000,false,14,63);

        builder.addSlot(RecipeIngredientRole.INPUT,84-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackABS()))
                .setFluidRenderer(64000,false,14,63);

        builder.addSlot(RecipeIngredientRole.OUTPUT,138-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackOUT()))
                .setFluidRenderer(64000,false,14,63);
    }

}
