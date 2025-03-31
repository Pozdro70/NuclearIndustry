package com.pozdro.nuclearindustry.fluid.deuterium;

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

public class ModFluidsDeuterium {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_DEUTERIUM = FLUIDS.register("deuterium_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsDeuterium.DEUTERIUM_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DEUTERIUM = FLUIDS.register("flowing_deuterium",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsDeuterium.DEUTERIUM_PROPERTIES));


    public static final ForgeFlowingFluid.Properties DEUTERIUM_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidDeuterium.DEUTERIUM_FLUID_TYPE, SOURCE_DEUTERIUM, FLOWING_DEUTERIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.DEUTERIUM_FLUID_BLOCK)
            .bucket(ModItems.DEUTERIUM_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
