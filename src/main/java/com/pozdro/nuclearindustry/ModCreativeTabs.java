package com.pozdro.nuclearindustry;

import com.pozdro.nuclearindustry.items.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static final CreativeModeTab MAIN_TAB = new CreativeModeTab("main") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.FILTER_UPGRADE.get());
        }
    };
    public static final CreativeModeTab FLUIDS_TAB = new CreativeModeTab("fluids") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.HYDROGEN_BUCKET.get());
        }
    };
}
