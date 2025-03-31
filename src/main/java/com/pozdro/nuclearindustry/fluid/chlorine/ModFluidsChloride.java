package com.pozdro.nuclearindustry.fluid.chlorine;

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

public class ModFluidsChloride {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_CHLORIDE = FLUIDS.register("chloride_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsChloride.CHLORIDE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CHLORIDE = FLUIDS.register("flowing_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsChloride.CHLORIDE_PROPERTIES));


    public static final ForgeFlowingFluid.Properties CHLORIDE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidChloride.CHLORIDE_FLUID_TYPE, SOURCE_CHLORIDE, FLOWING_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.CHLORIDE_FLUID_BLOCK)
            .bucket(ModItems.CHLORIDE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
