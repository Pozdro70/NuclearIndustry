package com.pozdro.nuclearindustry.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.screen.renderer.EnergyInfoArea;
import com.pozdro.nuclearindustry.screen.renderer.FluidTankRenderer;
import com.pozdro.nuclearindustry.util.MouseUtil;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;

public class OreWashingPlantScreen extends AbstractContainerScreen<OreWashingPlantMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(NuclearIndustry.MODID,"textures/gui/orewashingplant_gui.png");
    private EnergyInfoArea energyInfoArea;
    private FluidTankRenderer tankRenderer1;

    public OreWashingPlantScreen(OreWashingPlantMenu oreWashingPlantMenu, Inventory inventory, Component component) {
        super(oreWashingPlantMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        tankRenderer1 = new FluidTankRenderer(64000,true,30,63);
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x + 164, y + 6, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack,pMouseX,pMouseY,x,y);
        renderFluidAreaTooltips(pPoseStack,pMouseX,pMouseY,x,y);
    }

    private void renderFluidAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveAreaT1(pMouseX, pMouseY, x, y, 60, 6)) {
            renderTooltip(pPoseStack, tankRenderer1.getTooltip(menu.getFluidInINTank(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }

    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 164, 6, 4, 62)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(PoseStack stack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(stack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(stack, x, y);
        energyInfoArea.draw(stack);
        tankRenderer1.render(stack, x+60,y+6,menu.getFluidInINTank());
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 27, y + 9, 0, 166, menu.getScaledProgress(), 69);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveAreaT1(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, tankRenderer1.getWidth(), tankRenderer1.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
