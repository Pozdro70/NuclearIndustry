package com.pozdro.nuclearindustry.fluid.tritium;

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

public class ModFluidsTritium {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_TRITIUM = FLUIDS.register("tritium_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsTritium.TRITIUM_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TRITIUM = FLUIDS.register("flowing_tritium",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsTritium.TRITIUM_PROPERTIES));


    public static final ForgeFlowingFluid.Properties TRITIUM_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTritium.TRITIUM_FLUID_TYPE, SOURCE_TRITIUM, FLOWING_TRITIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.TRITIUM_FLUID_BLOCK)
            .bucket(ModItems.TRITIUM_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
