package com.pozdro.nuclearindustry.fluid.sulfuricacid;

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

public class ModFluidsSulfuricAcid {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_SULFURICACID = FLUIDS.register("sulfuricacid_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsSulfuricAcid.SULFURICACID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SULFURICACID = FLUIDS.register("flowing_sulfuricacid",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsSulfuricAcid.SULFURICACID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties SULFURICACID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidSulfuricAcid.SULFURIC_ACID_FLUID_TYPE, SOURCE_SULFURICACID, FLOWING_SULFURICACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SULFURICACID_FLUID_BLOCK)
            .bucket(ModItems.SULFURICACID_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
