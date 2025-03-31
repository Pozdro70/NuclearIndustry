package com.pozdro.nuclearindustry.fluid.purifiedwater;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsPurifiedWater {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_PURIFIEDWATER = FLUIDS.register("purifiedwater_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsPurifiedWater.PURIFIEDWATER_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PURIFIEDWATER = FLUIDS.register("flowing_purifiedwater",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsPurifiedWater.PURIFIEDWATER_PROPERTIES));


    public static final ForgeFlowingFluid.Properties PURIFIEDWATER_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidPurifiedWater.PURIFIEDWATER_FLUID_TYPE, SOURCE_PURIFIEDWATER, FLOWING_PURIFIEDWATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.PURIFIEDWATER_FLUID_BLOCK)
            .bucket(ModItems.PURIFIEDWATER_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
