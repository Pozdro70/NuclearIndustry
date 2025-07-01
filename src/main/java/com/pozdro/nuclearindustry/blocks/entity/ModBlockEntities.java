package com.pozdro.nuclearindustry.blocks.entity;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.blocks.custom.GasOvenBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENITIES=
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NuclearIndustry.MODID);

    public static final RegistryObject<BlockEntityType<ElechamberBlockEntity>> ELECHAMBER=
            BLOCK_ENITIES.register("elechamber",()-> BlockEntityType.Builder.of(ElechamberBlockEntity::new,
                    ModBlocks.ELECHAMBER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE=
            BLOCK_ENITIES.register("centrifuge",()-> BlockEntityType.Builder.of(CentrifugeBlockEntity::new,
                    ModBlocks.CENTRIFUGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<StillBlockEntity>> STILL=
            BLOCK_ENITIES.register("still",()-> BlockEntityType.Builder.of(StillBlockEntity::new,
                    ModBlocks.STILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ChemicalMixerBlockEntity>> CHEMICAL_MIXER=
            BLOCK_ENITIES.register("chemicalmixer",()-> BlockEntityType.Builder.of(ChemicalMixerBlockEntity::new,
                    ModBlocks.CHEMICALMIXER.get()).build(null));


    public static final RegistryObject<BlockEntityType<GasOvenBlockEntity>> GAS_OVEN=
            BLOCK_ENITIES.register("gasoven",()-> BlockEntityType.Builder.of(GasOvenBlockEntity::new,
                    ModBlocks.GAS_OVEN.get()).build(null));

    public static final RegistryObject<BlockEntityType<AbsorberBlockEntity>> ABSORBER=
            BLOCK_ENITIES.register("absorber",()-> BlockEntityType.Builder.of(AbsorberBlockEntity::new,
                    ModBlocks.ABSORBER.get()).build(null));

    public static final RegistryObject<BlockEntityType<OreWashingPlantBlockEntity>> ORE_WASHING_PLANT=
            BLOCK_ENITIES.register("orewashingplant",()-> BlockEntityType.Builder.of(OreWashingPlantBlockEntity::new,
                    ModBlocks.ORE_WASHING_PLANT.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENITIES.register(eventBus);
    }
}
