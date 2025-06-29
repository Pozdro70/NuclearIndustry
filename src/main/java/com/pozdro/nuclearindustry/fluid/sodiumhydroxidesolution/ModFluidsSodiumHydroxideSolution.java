package com.pozdro.nuclearindustry.fluid.sodiumhydroxidesolution;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.fluid.oxygen.ModFluidOxygen;
import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsSodiumHydroxideSolution {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_SODIUMHYDROXIDESOLUTION = FLUIDS.register("sodium_hydroxide_solution_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsSodiumHydroxideSolution.SODIUMHYDROXIDESOLUTION_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUMHYDROXIDESOLUTION = FLUIDS.register("flowing_sodium_hydroxide_solution",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsSodiumHydroxideSolution.SODIUMHYDROXIDESOLUTION_PROPERTIES));


    public static final ForgeFlowingFluid.Properties SODIUMHYDROXIDESOLUTION_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidSodiumHydroxideSolution.SODIUMHYDROXIDESOLUTION_FLUID_TYPE, SOURCE_SODIUMHYDROXIDESOLUTION, FLOWING_SODIUMHYDROXIDESOLUTION)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SODIUMHYDROXIDESOLUTION_FLUID_BLOCK)
            .bucket(ModItems.SODIUMHYDROXIDESOLUTION_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
