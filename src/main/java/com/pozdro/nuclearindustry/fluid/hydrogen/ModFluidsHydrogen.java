package com.pozdro.nuclearindustry.fluid.hydrogen;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidHeavyWater;
import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsHydrogen {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGEN = FLUIDS.register("hydrogen_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsHydrogen.HYDROGEN_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGEN = FLUIDS.register("flowing_hydrogen",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsHydrogen.HYDROGEN_PROPERTIES));


    public static final ForgeFlowingFluid.Properties HYDROGEN_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidHydrogen.HYDROGEN_FLUID_TYPE, SOURCE_HYDROGEN, FLOWING_HYDROGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.HYDROGEN_FLUID_BLOCK)
            .bucket(ModItems.HYDROGEN_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
