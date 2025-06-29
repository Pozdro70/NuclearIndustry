package com.pozdro.nuclearindustry.fluid.fumingsulfuricacid;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.fluid.deuterium.ModFluidDeuterium;
import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsFumingSulfuricAcid {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_FUMINGSULFURICACID = FLUIDS.register("fumingsulfuricacid_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsFumingSulfuricAcid.DEUTERIUM_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FUMINGSULFURICACID = FLUIDS.register("flowing_fumingsulfuricacid",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsFumingSulfuricAcid.DEUTERIUM_PROPERTIES));


    public static final ForgeFlowingFluid.Properties DEUTERIUM_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidFumingSulfuricAcid.FUMINGSULFURICACID_FLUID_TYPE, SOURCE_FUMINGSULFURICACID, FLOWING_FUMINGSULFURICACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.FUMINGSULFURICACID_FLUID_BLOCK)
            .bucket(ModItems.FUMINGSULFURICACID_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
