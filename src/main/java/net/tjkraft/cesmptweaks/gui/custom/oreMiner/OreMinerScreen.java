package net.tjkraft.cesmptweaks.gui.custom.oreMiner;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;

public class OreMinerScreen extends AbstractContainerScreen<OreMinerGUI> {
    private static final ResourceLocation GUI = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/ore_miner_gui.png");

    public OreMinerScreen(OreMinerGUI pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 203;
        this.imageHeight = 245;
        this.inventoryLabelY = 152;
    }

    private int getProgress(int pixels) {
        if (menu.maxProgress() == 0) return 0;
        return menu.progress() * pixels / menu.maxProgress();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI, x, y, 0, 0, imageWidth, imageHeight);
        int arrowSize = getProgress(22);
        if (arrowSize > 0) {
            pGuiGraphics.blit(GUI, leftPos + 80, topPos + 45, 206, 0, 15, arrowSize);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
