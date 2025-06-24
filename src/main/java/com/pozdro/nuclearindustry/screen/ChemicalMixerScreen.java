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

public class ChemicalMixerScreen extends AbstractContainerScreen<ChemicalMixerMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(NuclearIndustry.MODID,
            "textures/gui/chemical_mixer_gui.png");

    private EnergyInfoArea energyInfoArea;

    private FluidTankRenderer tankRenderer1;
    private FluidTankRenderer tankRenderer15;
    private FluidTankRenderer tankRenderer2;
    public ChemicalMixerScreen(ChemicalMixerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        tankRenderer1 = new FluidTankRenderer(64000,true,14,63);
        tankRenderer15 = new FluidTankRenderer(64000,true,14,63);
        tankRenderer2 = new FluidTankRenderer(64000,true,14,63);
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x+158,y+8,menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack,pMouseX,pMouseY,x,y);
        renderFluidAreaTooltips(pPoseStack,pMouseX,pMouseY,x,y);
    }

    private void renderFluidAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveAreaT1(pMouseX, pMouseY, x, y, 9, 8)) {
            renderTooltip(pPoseStack, tankRenderer1.getTooltip(menu.getFluidInINTank(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }

        if(isMouseAboveAreaT15(pMouseX, pMouseY, x, y, 26, 8)) {
            renderTooltip(pPoseStack, tankRenderer15.getTooltip(menu.getFluidInIN1Tank(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }

        if(isMouseAboveAreaT2(pMouseX, pMouseY, x, y, 116, 8)) {
            renderTooltip(pPoseStack, tankRenderer2.getTooltip(menu.getFluidInOUTTank(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 158, 8, 4, 62)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack,x,y);
        energyInfoArea.draw(pPoseStack);

        tankRenderer1.render(pPoseStack, x+9,y+8,menu.getFluidInINTank());
        tankRenderer15.render(pPoseStack, x+26,y+8,menu.getFluidInIN1Tank());
        tankRenderer2.render(pPoseStack, x+116,y+8,menu.getFluidInOUTTank());
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 60, y + 9, 176, 0,menu.getScaledProgress(),52 );
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack,pMouseX,pMouseY,delta);
        renderTooltip(pPoseStack,pMouseX,pMouseY);
    }

    private boolean isMouseAboveAreaT1(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, tankRenderer1.getWidth(), tankRenderer1.getHeight());
    }

    private boolean isMouseAboveAreaT15(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, tankRenderer15.getWidth(), tankRenderer15.getHeight());
    }

    private boolean isMouseAboveAreaT2(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, tankRenderer2.getWidth(), tankRenderer2.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
