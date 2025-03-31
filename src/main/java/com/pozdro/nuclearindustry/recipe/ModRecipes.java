package com.pozdro.nuclearindustry.recipe;

import com.pozdro.nuclearindustry.NuclearIndustry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NuclearIndustry.MODID);

    public static final RegistryObject<RecipeSerializer<ElechamberRecipe>> ELECHAMBER_SERIALIZER =
            SERIALIZERS.register("electrolysis", () -> ElechamberRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<StillRecipe>> STILL_SERIALIZER =
            SERIALIZERS.register("distillation", () -> StillRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<GasOvenRecipe>> GAS_OVEN_SERIALIZER =
            SERIALIZERS.register("gasoven", () -> GasOvenRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<AbsorberRecipe>> ABSORBER_SERIALIZER =
            SERIALIZERS.register("absorber", () -> AbsorberRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
