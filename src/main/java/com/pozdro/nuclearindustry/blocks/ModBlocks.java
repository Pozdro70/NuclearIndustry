package com.pozdro.nuclearindustry.blocks;

import com.pozdro.nuclearindustry.ModCreativeTabs;
import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.blocks.custom.*;
import com.pozdro.nuclearindustry.fluid.chlorine.ModFluidsChloride;
import com.pozdro.nuclearindustry.fluid.deuterium.ModFluidsDeuterium;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidsHeavyWater;
import com.pozdro.nuclearindustry.fluid.hydrochloricacid.ModFluidsHydrochloricAcid;
import com.pozdro.nuclearindustry.fluid.hydrogen.ModFluidsHydrogen;
import com.pozdro.nuclearindustry.fluid.hydrogenchloride.ModFluidHydrogenChloride;
import com.pozdro.nuclearindustry.fluid.hydrogenchloride.ModFluidsHydrogenChloride;
import com.pozdro.nuclearindustry.fluid.oxygen.ModFluidsOxygen;
import com.pozdro.nuclearindustry.fluid.purifiedwater.ModFluidsPurifiedWater;
import com.pozdro.nuclearindustry.fluid.sulfurdioxide.ModFluidsSulfurDioxide;
import com.pozdro.nuclearindustry.fluid.sulfurtrioxide.ModFluidSulfurTrioxide;
import com.pozdro.nuclearindustry.fluid.sulfurtrioxide.ModFluidsSulfurTrioxide;
import com.pozdro.nuclearindustry.fluid.tritium.ModFluidsTritium;
import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NuclearIndustry.MODID);



    public static final RegistryObject<Block> THERMAL_ISOLATOR = registerBlock("thermal_isolator",
            ()->new Block(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> COPPER_COIL = registerBlock("copper_coil",
            ()->new RotationalBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);


    public static final RegistryObject<Block> MACHINE_HULL = registerBlock("machine_hull",
            ()->new Block(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> SPODUMENE_ORE = registerBlock("spodumene_ore",
            ()->new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> SULFUR_ORE = registerBlock("sulfur_ore",
            ()->new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> DEEPSLATE_SPODUMENE_ORE = registerBlock("deepslate_spodumene_ore",
            ()->new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);
    public static final RegistryObject<Block> DEEPSLATE_SULDFUR_ORE = registerBlock("deepslate_sulfur_ore",
            ()->new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);
    public static final RegistryObject<Block> CHEMICALMIXER = registerBlock("chemicalmixer",
            ()->new ChemicalMixerBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> ELECHAMBER = registerBlock("elechamber",
            ()->new ElechamberBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops().noOcclusion()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> ABSORBER = registerBlock("absorber",
            ()->new AbsorberBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops().noOcclusion()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> STILL = registerBlock("still",
            ()->new StillBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops().noOcclusion()),
            ModCreativeTabs.MAIN_TAB);

    public static final RegistryObject<Block> GAS_OVEN = registerBlock("gasoven",
            ()->new GasOvenBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6f).requiresCorrectToolForDrops().noOcclusion()),
            ModCreativeTabs.MAIN_TAB);


    public static final RegistryObject<LiquidBlock> TRITIUM_FLUID_BLOCK = BLOCKS.register("tritium_fluid_block",
            ()->new LiquidBlock(ModFluidsTritium.SOURCE_TRITIUM, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> HYDROGEN_FLUID_BLOCK = BLOCKS.register("hydrogen_fluid_block",
            ()->new LiquidBlock(ModFluidsHydrogen.SOURCE_HYDROGEN, BlockBehaviour.Properties.copy(Blocks.WATER)));


    public static final RegistryObject<LiquidBlock> DEUTERIUM_FLUID_BLOCK = BLOCKS.register("deuterium_fluid_block",
            ()->new LiquidBlock(ModFluidsDeuterium.SOURCE_DEUTERIUM, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> HEAVYWATER_FLUID_BLOCK = BLOCKS.register("heavywater_fluid_block",
            ()->new LiquidBlock(ModFluidsHeavyWater.SOURCE_HEAVYWATER, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> CHLORIDE_FLUID_BLOCK = BLOCKS.register("chloride_fluid_block",
            ()->new LiquidBlock(ModFluidsChloride.SOURCE_CHLORIDE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> HYDROGENCHLORIDE_FLUID_BLOCK = BLOCKS.register("hydrogenchloride_fluid_block",
            ()->new LiquidBlock(ModFluidsHydrogenChloride.SOURCE_HYDROGENCHLORIDE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> PURIFIEDWATER_FLUID_BLOCK = BLOCKS.register("purifiedwater_fluid_block",
            ()->new LiquidBlock(ModFluidsPurifiedWater.SOURCE_PURIFIEDWATER, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> HYDROCHLORICACID_FLUID_BLOCK = BLOCKS.register("hydrochloricacid_fluid_block",
            ()->new LiquidBlock(ModFluidsHydrochloricAcid.SOURCE_HYDROCHLORICACID, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final RegistryObject<LiquidBlock> SULFURDIOXIDE_FLUID_BLOCK = BLOCKS.register("sulfurdioxide_fluid_block",
            ()->new LiquidBlock(ModFluidsSulfurDioxide.SOURCE_SULFURDIOXIDE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> OXYGEN_FLUID_BLOCK = BLOCKS.register("oxygen_fluid_block",
            ()->new LiquidBlock(ModFluidsOxygen.SOURCE_OXYGEN, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> SULFURTRIOXIDE_FLUID_BLOCK = BLOCKS.register("sulfurtrioxide_fluid_block",
            ()->new LiquidBlock(ModFluidsSulfurTrioxide.SOURCE_SULFURTRIOXIDE, BlockBehaviour.Properties.copy(Blocks.WATER)));


    private static <T extends  Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn,tab);
        return toReturn;
    }

    private static <T extends  Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name,()->new BlockItem(block.get(),new Item.Properties().tab(tab)));
    }


    public static void register(IEventBus ebus){BLOCKS.register(ebus);}
}
