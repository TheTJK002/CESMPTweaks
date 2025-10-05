package net.tjkraft.cesmptweaks.gui.custom.alveary;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
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

        if (menu.getFluidStack().isEmpty()) return;
        IClientFluidTypeExtensions type = IClientFluidTypeExtensions.of(menu.getFluidStack().getFluid());
        ResourceLocation textureStill = type.getStillTexture(menu.getFluidStack());

        TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textureStill);

        int tintColor = type.getTintColor(menu.getFluidStack().getFluid().defaultFluidState(), menu.alvearyBE.getLevel(), menu.alvearyBE.getBlockPos());
        float alpha = (tintColor >> 24 & 0xFF) / 255f;
        float red = (tintColor >> 16 & 0xFF) / 255f;
        float green = (tintColor >> 8 & 0xFF) / 255f;
        float blue = (tintColor & 0xFF) / 255f;

        pGuiGraphics.blit(leftPos + 102, topPos + 25 - Math.round(menu.alvearyBE.tank.getFluidAmount() / menu.alvearyBE.tank.getCapacity() * 50), 1, 16, 50 - Math.round(menu.alvearyBE.tank.getFluidAmount() / menu.alvearyBE.tank.getCapacity() * 50), spriteStill, red, green, blue, alpha);
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
            if(fluid.isEmpty()) return;
            pGuiGraphics.renderTooltip(this.font, Component.literal(fluid.getDisplayName().getString() + ": " + fluid.getAmount() + "/" + menu.alvearyBE.getTank().getCapacity() + " mB"), pMouseX, pMouseY);
        }
    }
}
