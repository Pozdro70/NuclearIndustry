package com.pozdro.nuclearindustry.fluid.oxygen;

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

public class ModFluidsOxygen {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_OXYGEN = FLUIDS.register("oxygen_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsOxygen.OXYGEN_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_OXYGEN = FLUIDS.register("flowing_oxygen",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsOxygen.OXYGEN_PROPERTIES));


    public static final ForgeFlowingFluid.Properties OXYGEN_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidOxygen.OXYGEN_FLUID_TYPE, SOURCE_OXYGEN, FLOWING_OXYGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.OXYGEN_FLUID_BLOCK)
            .bucket(ModItems.OXYGEN_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
