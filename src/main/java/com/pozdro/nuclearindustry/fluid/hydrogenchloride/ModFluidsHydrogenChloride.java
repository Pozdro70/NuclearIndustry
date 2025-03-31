package com.pozdro.nuclearindustry.fluid.hydrogenchloride;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.fluid.chlorine.ModFluidChloride;
import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsHydrogenChloride {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGENCHLORIDE = FLUIDS.register("hydrogenchloride_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsHydrogenChloride.HYDROGENCHLORIDE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGENCHLORIDE = FLUIDS.register("flowing_hydrogenchloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsHydrogenChloride.HYDROGENCHLORIDE_PROPERTIES));


    public static final ForgeFlowingFluid.Properties HYDROGENCHLORIDE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidHydrogenChloride.HYDROGENCHLORIDE_FLUID_TYPE, SOURCE_HYDROGENCHLORIDE, FLOWING_HYDROGENCHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.HYDROGENCHLORIDE_FLUID_BLOCK)
            .bucket(ModItems.HYDROGENCHLORIDE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
