package com.pozdro.nuclearindustry.world.feature;

import com.pozdro.nuclearindustry.NuclearIndustry;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, NuclearIndustry.MODID);


    public static final RegistryObject<PlacedFeature> ZIRCON_ORE_PLACED = PLACED_FEATURES.register("spodumene_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.SPODUMENE_ORE.getHolder().get(),
                    commonOrePlacement(12, // VeinsPerChunk
                            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));
    public static final RegistryObject<PlacedFeature> SULFUR_ORE_PLACED = PLACED_FEATURES.register("sulfur_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.SULFUR_ORE.getHolder().get(),
                    commonOrePlacement(17, // VeinsPerChunk
                            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(80)))));
    public static final RegistryObject<PlacedFeature> SYLVINITE_ORE_PLACED = PLACED_FEATURES.register("sylvinite_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.SYLVINITE_ORE.getHolder().get(),
                    commonOrePlacement(17, // VeinsPerChunk
                            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(80)))));



    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    public static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}
