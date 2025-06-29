package com.pozdro.nuclearindustry;

import com.mojang.logging.LogUtils;
import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.blocks.entity.ModBlockEntities;
import com.pozdro.nuclearindustry.fluid.BaseFluidType;
import com.pozdro.nuclearindustry.fluid.deuterium.ModFluidsDeuterium;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidsHeavyWater;
import com.pozdro.nuclearindustry.fluid.oxygen.ModFluidsOxygen;
import com.pozdro.nuclearindustry.fluid.tritium.ModFluidsTritium;
import com.pozdro.nuclearindustry.items.ModItems;
import com.pozdro.nuclearindustry.networking.ModMessages;
import com.pozdro.nuclearindustry.recipe.ModRecipes;
import com.pozdro.nuclearindustry.screen.*;
import com.pozdro.nuclearindustry.world.feature.ModConfiguredFeatures;
import com.pozdro.nuclearindustry.world.feature.ModPlacedFeatures;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NuclearIndustry.MODID)
public class NuclearIndustry
{
    //test
    // Define mod id in a common place for everything to reference
    public static final String MODID = "nuclearindustry";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "nuclearindustry" namespace

    public NuclearIndustry(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        BaseFluidType.registerAllFluids(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->{
            ModMessages.register();
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {


            ItemBlockRenderTypes.setRenderLayer(ModFluidsDeuterium.SOURCE_DEUTERIUM.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluidsDeuterium.FLOWING_DEUTERIUM.get(),RenderType.translucent());

            ItemBlockRenderTypes.setRenderLayer(ModFluidsTritium.SOURCE_TRITIUM.get(),RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluidsTritium.FLOWING_TRITIUM.get(),RenderType.translucent());

            ItemBlockRenderTypes.setRenderLayer(ModFluidsHeavyWater.SOURCE_HEAVYWATER.get(),RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluidsHeavyWater.FLOWING_HEAVYWATER.get(),RenderType.translucent());

            ItemBlockRenderTypes.setRenderLayer(ModFluidsOxygen.SOURCE_OXYGEN.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluidsOxygen.FLOWING_OXYGEN.get(),RenderType.translucent());


            MenuScreens.register(ModMenuTypes.ELECHAMBER_MENU.get(), ElechamberScreen::new);
            MenuScreens.register(ModMenuTypes.STILL_MENU.get(), StillScreen::new);
            MenuScreens.register(ModMenuTypes.CHEMICAL_MIXER_MENU.get(), ChemicalMixerScreen::new);
            MenuScreens.register(ModMenuTypes.GASOVEN_MENU.get(), GasOvenScreen::new);
            MenuScreens.register(ModMenuTypes.ABSORBER_MENU.get(), AbsorberScreen::new);
            MenuScreens.register(ModMenuTypes.ORE_WASHING_PLANT_MENU.get(), OreWashingPlantScreen::new);
        }
    }
}
