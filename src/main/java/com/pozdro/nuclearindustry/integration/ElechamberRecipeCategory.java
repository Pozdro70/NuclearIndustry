package com.pozdro.nuclearindustry.integration;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.items.ModItems;
import com.pozdro.nuclearindustry.recipe.ElechamberRecipe;
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

public class ElechamberRecipeCategory implements IRecipeCategory<ElechamberRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(NuclearIndustry.MODID, "electrolysis");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(NuclearIndustry.MODID, "textures/gui/elechamber_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ElechamberRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE,4,4,169,78);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModBlocks.ELECHAMBER.get()));
    }

    @Override
    public RecipeType<ElechamberRecipe> getRecipeType() {
        return JEINuclearIndustryPlugin.ELECTROLYSIS;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Electrolysis");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ElechamberRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,7-4,6-4); //0
        if(recipe.getId().toString().equals("nuclearindustry:heavy_water_from_electrolysis")){
            builder.addSlot(RecipeIngredientRole.OUTPUT,7-4,53-4)
                    .addItemStack(new ItemStack(ModItems.FILTER_UPGRADE.get(),1));//1
        } else if (recipe.getId().toString().equals("nuclearindustry:chloride_from_electrolysis"))
        {
            builder.addSlot(RecipeIngredientRole.OUTPUT,7-4,53-4)
                    .addItemStack(new ItemStack(ModItems.FILTER_UPGRADE.get(),1));//1
        } else{
            builder.addSlot(RecipeIngredientRole.OUTPUT,7-4,53-4);//1
        }

        builder.addSlot(RecipeIngredientRole.INPUT,56-4,53-4).addIngredients(recipe.getIngredients().get(0));//2
        builder.addSlot(RecipeIngredientRole.OUTPUT,116-4,53-4).addItemStack(recipe.getResultItem());//3

        builder.addSlot(RecipeIngredientRole.INPUT,28-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackIN()))
                .setFluidRenderer(64000,false,14,63);

        builder.addSlot(RecipeIngredientRole.OUTPUT,138-4,6-4).addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(recipe.getFluidStackOUT()))
                .setFluidRenderer(64000,false,14,63);
    }

}
