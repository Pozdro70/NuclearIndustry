package com.pozdro.nuclearindustry.items;

import com.pozdro.nuclearindustry.ModCreativeTabs;
import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.fluid.chlorine.ModFluidsChloride;
import com.pozdro.nuclearindustry.fluid.deuterium.ModFluidsDeuterium;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidsHeavyWater;
import com.pozdro.nuclearindustry.fluid.hydrochloricacid.ModFluidsHydrochloricAcid;
import com.pozdro.nuclearindustry.fluid.hydrogen.ModFluidHydrogen;
import com.pozdro.nuclearindustry.fluid.hydrogen.ModFluidsHydrogen;
import com.pozdro.nuclearindustry.fluid.hydrogenchloride.ModFluidsHydrogenChloride;
import com.pozdro.nuclearindustry.fluid.purifiedwater.ModFluidsPurifiedWater;
import com.pozdro.nuclearindustry.fluid.sulfurdioxide.ModFluidsSulfurDioxide;
import com.pozdro.nuclearindustry.fluid.tritium.ModFluidsTritium;
import com.pozdro.nuclearindustry.items.custom.UpgradeItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NuclearIndustry.MODID);

    public static final RegistryObject<Item> STEEL = ITEMS.register("steel",
            ()->new Item(new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MAIN_TAB)));

    public static final RegistryObject<Item> SALT = ITEMS.register("salt",
            ()->new Item(new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MAIN_TAB)));

    public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur",
            ()->new Item(new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MAIN_TAB)));

    public static final RegistryObject<Item> SPODUMENE = ITEMS.register("spodumene",
            ()->new Item(new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MAIN_TAB)));

    public static final RegistryObject<Item> RAW_SPODUMENE = ITEMS.register("raw_spodumene",
            ()->new Item(new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MAIN_TAB)));

    public static final RegistryObject<Item> SODIUM_HYDROXIDE = ITEMS.register("sodium_hydroxide",
            ()->new Item(new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MAIN_TAB)));

    public static final RegistryObject<Item> FILTER_UPGRADE = ITEMS.register("filter_upgrade",
            ()->new UpgradeItem(new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MAIN_TAB)));

    public static final RegistryObject<Item> NoItem = ITEMS.register("nothing",
            ()->new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DEUTERIUM_BUCKET = ITEMS.register("deuterium_bucket",
            ()->new BucketItem(ModFluidsDeuterium.SOURCE_DEUTERIUM,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> HYDROGEN_BUCKET = ITEMS.register("hydrogen_bucket",
            ()->new BucketItem(ModFluidsHydrogen.SOURCE_HYDROGEN,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));
    public static final RegistryObject<Item> TRITIUM_BUCKET = ITEMS.register("tritium_bucket",
            ()->new BucketItem(ModFluidsTritium.SOURCE_TRITIUM,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> HEAVYWATER_BUCKET = ITEMS.register("heavywater_bucket",
            ()->new BucketItem(ModFluidsHeavyWater.SOURCE_HEAVYWATER,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> CHLORIDE_BUCKET = ITEMS.register("chloride_bucket",
            ()->new BucketItem(ModFluidsChloride.SOURCE_CHLORIDE,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> PURIFIEDWATER_BUCKET = ITEMS.register("purifiedwater_bucket",
            ()->new BucketItem(ModFluidsPurifiedWater.SOURCE_PURIFIEDWATER,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> HYDROGENCHLORIDE_BUCKET = ITEMS.register("hydrogenchloride_bucket",
            ()->new BucketItem(ModFluidsHydrogenChloride.SOURCE_HYDROGENCHLORIDE,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> HYDROCHLORICACID_BUCKET = ITEMS.register("hydrochloricacid_bucket",
            ()->new BucketItem(ModFluidsHydrochloricAcid.SOURCE_HYDROCHLORICACID,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> SULFURDIOXIDE_BUCKET = ITEMS.register("sulfurdioxide_bucket",
            ()->new BucketItem(ModFluidsSulfurDioxide.SOURCE_SULFURDIOXIDE,new Item.Properties().stacksTo(1)
                    .tab(ModCreativeTabs.MAIN_TAB).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus ebus){ITEMS.register(ebus);}
}
