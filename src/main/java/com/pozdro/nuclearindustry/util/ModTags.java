package com.pozdro.nuclearindustry.util;

import com.pozdro.nuclearindustry.NuclearIndustry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static class Blocks{
        //public static final TagKey<Block> MACHINES = tag("machines");

        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(NuclearIndustry.MODID,name));
        }
    }

    public static class Items{
        private static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(NuclearIndustry.MODID,name));
        }
    }

    public static class Fluids{
        public static final TagKey<Fluid> STILL_INP_FLUIDS = tag("still_inp_fluids");

        public static final TagKey<Fluid> CHEMICAL_MIXER_INP_FLUIDS = tag("chemicalmixer_inp_fluids");
        public static final TagKey<Fluid> CHEMICAL_MIXER_INP1_FLUIDS = tag("chemicalmixer_inp1_fluids");
        public static final TagKey<Fluid> CHEMICAL_MIXER_INP2_FLUIDS = tag("chemicalmixer_inp2_fluids");
        public static final TagKey<Fluid> ELECHAMBER_INP_FLUIDS = tag("elechamber_inp_fluids");

        public static final TagKey<Fluid> OVEN_INP_FLUIDS = tag("oven_inp_fluids");
        public static final TagKey<Fluid> OVEN_INP2_FLUIDS = tag("oven2_inp_fluids");
        public static final TagKey<Fluid> ABSORBER_INP_FLUIDS = tag("absorber_inp_fluids");
        public static final TagKey<Fluid> ABSORBER_ABS_FLUIDS = tag("absorber_abs_fluids");

        private static TagKey<Fluid> tag(String name){
            return FluidTags.create(new ResourceLocation(NuclearIndustry.MODID,name));
        }
    }
}
