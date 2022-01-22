package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author LatvianModder
 */
public class ImageButton extends Widget {
	private ResourceLocation texture;
	private ResourceLocation hoverTexture;

	public ImageButton() {
		z = 10;
		setWidth(20);
		setHeight(20);
	}

	public void setTexture(ResourceLocation tex) {
		texture = tex;
		hoverTexture = texture;
	}

	public void setHoverTexture(ResourceLocation tex) {
		hoverTexture = tex;
	}

	@Override
	public void renderBackground(PoseStack matrixStack, float partialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, getUi().widgetTexture);
		RenderSystem.setShaderColor(1F, 1F, 1F, alpha / 255F);
		int i;
		int j;

		if (!enabled || getAction() == null) {
			i = 0;
			j = 0xCCCCCC;
		} else if (isMouseOver) {
			i = 2;
			j = hoverColor;
		} else {
			i = 1;
			j = color;
		}

		int cr = (j >> 16) & 255;
		int cg = (j >> 8) & 255;
		int cb = (j >> 0) & 255;
		int ca = alpha;

		int w = getWidth();
		int h = getHeight();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		matrixStack.pushPose();
		matrixStack.translate(actualX, actualY, z);
		blit(matrixStack, 0, 0, 0, 46 + i * 20, w / 2, h);
		blit(matrixStack, w / 2, 0, 200 - w / 2, 46 + i * 20, w / 2, h);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.setShaderTexture(0, isMouseOver ? hoverTexture : texture);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder builder = tessellator.getBuilder();
		Matrix4f m = matrixStack.last().pose();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
		builder.vertex(m, 1F, h - 1F, 0F).color(cr, cg, cb, ca).uv(0F, 1F).endVertex();
		builder.vertex(m, w - 1F, h - 1F, 0F).color(cr, cg, cb, ca).uv(1F, 1F).endVertex();
		builder.vertex(m, w - 1F, 1F, 0F).color(cr, cg, cb, ca).uv(1F, 0F).endVertex();
		builder.vertex(m, 1F, 1F, 0F).color(cr, cg, cb, ca).uv(0F, 0F).endVertex();
		tessellator.end();
		matrixStack.popPose();
	}
}