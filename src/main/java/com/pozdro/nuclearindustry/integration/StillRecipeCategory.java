package com.pozdro.nuclearindustry.integration;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
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

public class StillRecipeCategory implements IRecipeCategory<StillRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(NuclearIndustry.MODID, "distillation");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(NuclearIndustry.MODID, "textures/gui/still_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public StillRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE,4,4,169,78);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModBlocks.STILL.get()));
    }

    @Override
    public RecipeType<StillRecipe> getRecipeType() {
        return JEINuclearIndustryPlugin.DISTILATION;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Still");
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
    public void setRecipe(IRecipeLayoutBuilder builder, StillRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,9-4,8-4); //0
        builder.addSlot(RecipeIngredientRole.INPUT,48-4,55-4).addIngredients(recipe.getIngredients().get(0));//1
        builder.addSlot(RecipeIngredientRole.OUTPUT,135-4,55-4).addItemStack(recipe.getResultItem());//2


        builder.addSlot(RecipeIngredientRole.INPUT,29-4,8-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackIN()))
                .setFluidRenderer(64000,false,14,63);

        builder.addSlot(RecipeIngredientRole.OUTPUT,116-4,8-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackOUT()))
                .setFluidRenderer(64000,false,14,63);



    }
}
