package com.pozdro.nuclearindustry.fluid.sulfurdioxide;

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

public class ModFluidsSulfurDioxide {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_SULFURDIOXIDE = FLUIDS.register("sulfurdioxide_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsSulfurDioxide.SULFURDIOXIDE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SULFURDIOXIDE = FLUIDS.register("flowing_sulfurdioxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsSulfurDioxide.SULFURDIOXIDE_PROPERTIES));


    public static final ForgeFlowingFluid.Properties SULFURDIOXIDE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidSulfurDioxide.SULFURDIOXIDE_FLUID_TYPE, SOURCE_SULFURDIOXIDE, FLOWING_SULFURDIOXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SULFURDIOXIDE_FLUID_BLOCK)
            .bucket(ModItems.SULFURDIOXIDE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
