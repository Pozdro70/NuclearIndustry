package com.pozdro.nuclearindustry.world.feature;

import com.google.common.base.Suppliers;
import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, NuclearIndustry.MODID);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_SPODUMENE_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.SPODUMENE_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_SPODUMENE_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_SULFUR_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.SULFUR_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_SYLVINITE_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.SYLVINITE_ORE.get().defaultBlockState())));

    public static final RegistryObject<ConfiguredFeature<?, ?>> SPODUMENE_ORE = CONFIGURED_FEATURES.register("spodumene_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_SPODUMENE_ORES.get(),8)));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SULFUR_ORE = CONFIGURED_FEATURES.register("sulfur_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_SULFUR_ORES.get(),10)));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SYLVINITE_ORE = CONFIGURED_FEATURES.register("sylvinite_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_SYLVINITE_ORES.get(),10)));

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}
