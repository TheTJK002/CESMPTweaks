package net.tjkraft.cesmptweaks.gui.custom.alveary;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;

public class AlvearyScreen extends AbstractContainerScreen<AlvearyGUI> {
    private static final ResourceLocation GUI = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/alveary_gui.png");

    public AlvearyScreen(AlvearyGUI pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 182;
        this.inventoryLabelY = 88;
    }

    private int getProgress(int pixels) {
        if (menu.getMaxProgress() == 0) return 0;
        return menu.getProgress() * pixels / menu.getMaxProgress();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI, x, y, 0, 0, imageWidth, imageHeight);

        int honeyDropSize = getProgress(7);
        if (honeyDropSize > 0) {
            pGuiGraphics.blit(GUI, leftPos + 86, topPos + 45 + (7 - honeyDropSize), 176, (7 - honeyDropSize), 5, honeyDropSize);
        }

        FluidStack fluid = menu.getFluidStack();
        if (fluid.isEmpty()) return;

        int tankX = leftPos + 102;
        int tankY = topPos + 25;
        int tankW = 16;
        int tankH = 50;

        int filled = Math.round((float) fluid.getAmount() / (float) menu.alvearyBE.getTank().getCapacity() * tankH);
        int drawY = tankY + (tankH - filled);

        renderFluidTiled(pGuiGraphics, fluid, tankX, drawY, tankW, filled);
    }

    private void renderFluidTiled(GuiGraphics gui, FluidStack stack, int x, int y, int width, int height) {
        if (stack.isEmpty() || width <= 0 || height <= 0) return;

        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(stack.getFluid());
        ResourceLocation stillLoc = props.getStillTexture(stack);
        if (stillLoc == null) stillLoc = props.getFlowingTexture(stack);
        if (stillLoc == null) return;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillLoc);

        int tint = props.getTintColor(stack); // ARGB
        float a = ((tint >> 24) & 0xFF) / 255f;
        float r = ((tint >> 16) & 0xFF) / 255f;
        float g = ((tint >> 8) & 0xFF) / 255f;
        float b = (tint & 0xFF) / 255f;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        final int TILE = 16;
        for (int xx = 0; xx < width; xx += TILE) {
            for (int yy = 0; yy < height; yy += TILE) {
                int drawW = Math.min(TILE, width - xx);
                int drawH = Math.min(TILE, height - yy);
                if (drawW <= 0 || drawH <= 0) continue;

                int drawX = x + xx;
                int drawY = y + yy;

                gui.blit(drawX, drawY, 0, drawW, drawH, sprite, r, g, b, a);
            }
        }

        RenderSystem.disableBlend();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        int tankX = leftPos + 102;
        int tankY = topPos + 25;
        int width = 18;
        int height = 52;

        if (pMouseX >= tankX && pMouseX < tankX + width && pMouseY >= tankY && pMouseY < tankY + height) {
            FluidStack fluid = menu.getFluidStack();
            if (fluid.isEmpty()) return;
            pGuiGraphics.renderTooltip(this.font, Component.literal(fluid.getDisplayName().getString() + ": " + fluid.getAmount() + "/" + menu.alvearyBE.getTank().getCapacity() + " mB"), pMouseX, pMouseY);
        }
    }
}
