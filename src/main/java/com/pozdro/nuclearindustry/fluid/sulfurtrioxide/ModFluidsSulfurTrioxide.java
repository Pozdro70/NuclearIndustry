package com.pozdro.nuclearindustry.fluid.sulfurtrioxide;

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

public class ModFluidsSulfurTrioxide {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_SULFURTRIOXIDE = FLUIDS.register("sulfurtrioxide_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsSulfurTrioxide.SULFURTRIOXIDE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SULFURTRIOXIDE = FLUIDS.register("flowing_sulfurtrioxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsSulfurTrioxide.SULFURTRIOXIDE_PROPERTIES));


    public static final ForgeFlowingFluid.Properties SULFURTRIOXIDE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidSulfurTrioxide.SULFURTRIOXIDE_FLUID_TYPE, SOURCE_SULFURTRIOXIDE, FLOWING_SULFURTRIOXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SULFURTRIOXIDE_FLUID_BLOCK)
            .bucket(ModItems.SULFURTRIOXIDE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
