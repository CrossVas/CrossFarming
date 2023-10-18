package dev.crossvas.farming.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.crossvas.farming.CrossFarming;
import dev.crossvas.farming.gui.menus.CropCombineMenu;
import dev.crossvas.farming.utils.helpers.Helpers;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CropCombineScreen extends AbstractContainerScreen<CropCombineMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CrossFarming.ID,"textures/gui/combine.png");

    public CropCombineScreen(CropCombineMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(stack, x, y, 0, 0, imageWidth, imageHeight);
        renderEnergyBox(stack, x, y);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
        renderEnergyInfo(stack, x, y, mouseX, mouseY);
    }

    private void renderEnergyBox(PoseStack stack, int x, int y) {
        int boxSize = 16;
        int pX = 152 + x;
        int pY = 8 + y;
        if (menu.hasEnergy(menu.data.get(0))) {
            fillGradient(stack, pX, pY + boxSize - menu.getScaledEnergyBar(menu.data.get(0)), pX + boxSize, pY + boxSize, 0xff00b500, 0xff32cd32);
        }
    }

    public void renderEnergyInfo(PoseStack transform, int xPos, int yPos, int mXPos, int mYPos) {
        int pX = 152 + xPos;
        int pY = 8 + yPos;
        if (Helpers.isMouseOver(mXPos, mYPos, pX, pY, 16)) {
            renderTooltip(transform, Component.literal(menu.data.get(0) + "/" + menu.CROP_FARM_COMBINE.getEnergyStorage().getMaxEnergyStored() + " RF"),
                    mXPos, mYPos);
        }
    }
}
