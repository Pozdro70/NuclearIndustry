package com.pozdro.nuclearindustry.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.util.FluidJSONUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class ElechamberRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final FluidStack fluidStackIN;
    private final FluidStack fluidStackOUT;

    public ElechamberRecipe(ResourceLocation id, ItemStack output,
                                    NonNullList<Ingredient> recipeItems,FluidStack fluidStackIN,FluidStack fluidStackOUT) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.fluidStackIN = fluidStackIN;
        this.fluidStackOUT = fluidStackOUT;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()){
            return false;
        }

        return recipeItems.get(0).test(pContainer.getItem(2));
    }

    public FluidStack getFluidStackIN(){
        return fluidStackIN;
    }

    public FluidStack getFluidStackOUT(){
        return fluidStackOUT;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ElechamberRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "electrolysis";
    }


    public static class Serializer implements RecipeSerializer<ElechamberRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(NuclearIndustry.MODID, "electrolysis");

        @Override
        public ElechamberRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            FluidStack fluidIN = FluidJSONUtil.readFluid(pSerializedRecipe.get("fluidIN").getAsJsonObject());
            FluidStack fluidOUT = FluidJSONUtil.readFluid(pSerializedRecipe.get("fluidOUT").getAsJsonObject());

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ElechamberRecipe(pRecipeId, output, inputs,fluidIN,fluidOUT);
        }

        @Override
        public @Nullable ElechamberRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            FluidStack fluidIN = buf.readFluidStack();
            FluidStack fluidOUT = buf.readFluidStack();


            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new ElechamberRecipe(id, output, inputs,fluidIN,fluidOUT);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ElechamberRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            buf.writeFluidStack(recipe.fluidStackIN);
            buf.writeFluidStack(recipe.fluidStackOUT);

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);

        }
    }
}
