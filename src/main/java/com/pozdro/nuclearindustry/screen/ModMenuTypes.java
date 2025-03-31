package com.pozdro.nuclearindustry.screen;

import com.pozdro.nuclearindustry.NuclearIndustry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, NuclearIndustry.MODID);

    public static final RegistryObject<MenuType<ElechamberMenu>> ELECHAMBER_MENU =
            registerMenuType(ElechamberMenu::new, "elechamber_menu");

    public static final RegistryObject<MenuType<StillMenu>> STILL_MENU =
            registerMenuType(StillMenu::new, "still_menu");

    public static final RegistryObject<MenuType<GasOvenMenu>> GASOVEN_MENU =
            registerMenuType(GasOvenMenu::new, "gasoven_menu");

    public static final RegistryObject<MenuType<AbsorberMenu>> ABSORBER_MENU =
            registerMenuType(AbsorberMenu::new, "absorber_menu");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
