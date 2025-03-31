package com.pozdro.nuclearindustry.fluid.hydrochloricacid;

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

public class ModFluidsHydrochloricAcid {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NuclearIndustry.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_HYDROCHLORICACID = FLUIDS.register("hydrochloricacid_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluidsHydrochloricAcid.HYDROCHLORICACID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROCHLORICACID = FLUIDS.register("flowing_hydrochloricacid",
            () -> new ForgeFlowingFluid.Flowing(ModFluidsHydrochloricAcid.HYDROCHLORICACID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties HYDROCHLORICACID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidHydrochloricAcid.HYDROCHLORICACID_FLUID_TYPE, SOURCE_HYDROCHLORICACID, FLOWING_HYDROCHLORICACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.HYDROCHLORICACID_FLUID_BLOCK)
            .bucket(ModItems.HYDROCHLORICACID_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
