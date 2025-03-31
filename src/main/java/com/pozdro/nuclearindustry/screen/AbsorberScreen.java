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

public class AbsorberScreen extends AbstractContainerScreen<AbsorberMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(NuclearIndustry.MODID,
            "textures/gui/absorber_gui.png");

    private EnergyInfoArea energyInfoArea;

    private FluidTankRenderer tankRendererIN;
    private FluidTankRenderer tankRendererABS;
    private FluidTankRenderer tankRendererOUT;
    public AbsorberScreen(AbsorberMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        tankRendererIN= new FluidTankRenderer(64000,true,14,63);
        tankRendererABS= new FluidTankRenderer(64000,true,14,63);
        tankRendererOUT = new FluidTankRenderer(64000,true,14,63);
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x+164,y+6,menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack,pMouseX,pMouseY,x,y);
        renderFluidAreaTooltips(pPoseStack,pMouseX,pMouseY,x,y);
    }

    private void renderFluidAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveAreaTIN(pMouseX, pMouseY, x, y, 28, 6)) {
            renderTooltip(pPoseStack, tankRendererIN.getTooltip(menu.getFluidInINTank(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }

        if(isMouseAboveAreaTABS(pMouseX, pMouseY, x, y, 84, 6)) {
            renderTooltip(pPoseStack, tankRendererABS.getTooltip(menu.getFluidInAbsTank(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }

        if(isMouseAboveAreaTOUT(pMouseX, pMouseY, x, y, 138, 6)) {
            renderTooltip(pPoseStack, tankRendererOUT.getTooltip(menu.getFluidInOUTTank(), TooltipFlag.Default.NORMAL),
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

        tankRendererIN.render(pPoseStack, x+28,y+6,menu.getFluidInINTank());
        tankRendererABS.render(pPoseStack, x+84,y+6,menu.getFluidInAbsTank());
        tankRendererOUT.render(pPoseStack, x+138,y+6,menu.getFluidInOUTTank());
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 43, y + 8,0 , 166,menu.getScaledProgress(),70);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack,pMouseX,pMouseY,delta);
        renderTooltip(pPoseStack,pMouseX,pMouseY);
    }

    private boolean isMouseAboveAreaTIN(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, tankRendererIN.getWidth(), tankRendererIN.getHeight());
    }

    private boolean isMouseAboveAreaTABS(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, tankRendererABS.getWidth(), tankRendererABS.getHeight());
    }

    private boolean isMouseAboveAreaTOUT(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, tankRendererOUT.getWidth(), tankRendererOUT.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
