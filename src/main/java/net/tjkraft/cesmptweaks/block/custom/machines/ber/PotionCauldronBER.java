package net.tjkraft.cesmptweaks.block.custom.machines.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.tjkraft.cesmptweaks.blockTile.custom.PotionCauldronBE;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PotionCauldronBER implements BlockEntityRenderer<PotionCauldronBE> {
    public PotionCauldronBER(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(PotionCauldronBE be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        //InputTank
        renderFluidSurface(be.getInputTank().getFluid(), be.getInputLevelFraction(), poseStack, buffer, combinedLight, combinedOverlay, 2f / 16f, 6f / 16f);

        //OutputTank
        renderFluidSurface(be.getOutputTank().getFluid(), be.getOutputLevelFraction(), poseStack, buffer, combinedLight, combinedOverlay, 2f / 16f, 6f / 16f);
    }

    private void renderFluidSurface(FluidStack fluid, float fraction, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, float minY, float maxY) {
        if (fluid == null || fluid.isEmpty() || fraction <= 0) return;

        var tex = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid);
        if (tex == null) return;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(tex);
        if (sprite == null) return;

        int tint = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
        int a = (tint >> 24) & 0xFF;
        if (a == 0) a = 0xFF;
        int r = (tint >> 16) & 0xFF;
        int g = (tint >> 8) & 0xFF;
        int b = tint & 0xFF;

        float x1 = 3f / 16f, x2 = 13f / 16f;
        float z1 = 3f / 16f, z2 = 13f / 16f;

        float y = minY + (maxY - minY) * fraction;

        poseStack.pushPose();
        Matrix4f mat = poseStack.last().pose();
        Matrix3f normalMat = poseStack.last().normal();

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));

        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        //Top
        consumer.vertex(mat, x1, y, z2).color(r, g, b, a).uv(uMin, vMin).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMat, 0f, 1f, 0f).endVertex();
        consumer.vertex(mat, x2, y, z2).color(r, g, b, a).uv(uMax, vMin).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMat, 0f, 1f, 0f).endVertex();
        consumer.vertex(mat, x2, y, z1).color(r, g, b, a).uv(uMax, vMax).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMat, 0f, 1f, 0f).endVertex();
        consumer.vertex(mat, x1, y, z1).color(r, g, b, a).uv(uMin, vMax).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMat, 0f, 1f, 0f).endVertex();

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(PotionCauldronBE be) {
        return true;
    }

}