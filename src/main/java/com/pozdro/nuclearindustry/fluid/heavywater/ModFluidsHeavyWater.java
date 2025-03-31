package com.pozdro.nuclearindustry.fluid.heavywater;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.fluid.tritium.ModFluidTritium;
import com.pozdro.nuclearindustry.fluid.tritium.ModFluidsTritium;
import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsHeavyWater {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_HEAVYWATER = FLUIDS.register("heavywater_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsHeavyWater.HEAVYWATER_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HEAVYWATER = FLUIDS.register("flowing_heavywater",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsHeavyWater.HEAVYWATER_PROPERTIES));


    public static final ForgeFlowingFluid.Properties HEAVYWATER_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidHeavyWater.HEAVYWATER_FLUID_TYPE, SOURCE_HEAVYWATER, FLOWING_HEAVYWATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.HEAVYWATER_FLUID_BLOCK)
            .bucket(ModItems.HEAVYWATER_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
