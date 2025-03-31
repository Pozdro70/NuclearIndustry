package com.pozdro.nuclearindustry.fluid;


import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import com.pozdro.nuclearindustry.fluid.chlorine.ModFluidChloride;
import com.pozdro.nuclearindustry.fluid.chlorine.ModFluidsChloride;
import com.pozdro.nuclearindustry.fluid.deuterium.ModFluidDeuterium;
import com.pozdro.nuclearindustry.fluid.deuterium.ModFluidsDeuterium;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidHeavyWater;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidsHeavyWater;
import com.pozdro.nuclearindustry.fluid.hydrochloricacid.ModFluidHydrochloricAcid;
import com.pozdro.nuclearindustry.fluid.hydrochloricacid.ModFluidsHydrochloricAcid;
import com.pozdro.nuclearindustry.fluid.hydrogen.ModFluidHydrogen;
import com.pozdro.nuclearindustry.fluid.hydrogen.ModFluidsHydrogen;
import com.pozdro.nuclearindustry.fluid.hydrogenchloride.ModFluidHydrogenChloride;
import com.pozdro.nuclearindustry.fluid.hydrogenchloride.ModFluidsHydrogenChloride;
import com.pozdro.nuclearindustry.fluid.purifiedwater.ModFluidPurifiedWater;
import com.pozdro.nuclearindustry.fluid.purifiedwater.ModFluidsPurifiedWater;
import com.pozdro.nuclearindustry.fluid.tritium.ModFluidTritium;
import com.pozdro.nuclearindustry.fluid.tritium.ModFluidsTritium;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Basic implementation of {@link FluidType} that supports specifying still and flowing textures in the constructor.
 *
 * @author Choonster (<a href="https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.19.x/LICENSE.txt">MIT License</a>)
 *
 * Change by: Kaupenjoe
 * Added overlayTexture and tintColor as well. Also converts tint color into fog color
 *
 * fix by pozdro - ability to add more than one fluid
 */
public class BaseFluidType extends  FluidType{

    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final int tintColor;
    private final Vector3f fogColor;

    public BaseFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final ResourceLocation overlayTexture,
                         final int tintColor, final Vector3f fogColor, final Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.tintColor = tintColor;
        this.fogColor = fogColor;
    }

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public int getTintColor() {
        return tintColor;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        });
    }

    public static void registerAllFluids(IEventBus eventBus){
        ModFluidsHeavyWater.register(eventBus);
        ModFluidHeavyWater.register(eventBus);
        ModFluidsDeuterium.register(eventBus);
        ModFluidDeuterium.register(eventBus);
        ModFluidsTritium.register(eventBus);
        ModFluidTritium.register(eventBus);
        ModFluidsHydrogen.register(eventBus);
        ModFluidHydrogen.register(eventBus);
        ModFluidChloride.register(eventBus);
        ModFluidsChloride.register(eventBus);
        ModFluidHydrogenChloride.register(eventBus);
        ModFluidsHydrogenChloride.register(eventBus);
        ModFluidPurifiedWater.register(eventBus);
        ModFluidsPurifiedWater.register(eventBus);
        ModFluidHydrochloricAcid.register(eventBus);
        ModFluidsHydrochloricAcid.register(eventBus);
    }
}
