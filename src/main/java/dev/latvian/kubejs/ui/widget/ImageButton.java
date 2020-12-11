package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class ImageButton extends Widget
{
	private ResourceLocation texture;
	private ResourceLocation hoverTexture;

	public ImageButton()
	{
		z = 10;
		setWidth(20);
		setHeight(20);
	}

	public void setTexture(String tex)
	{
		texture = UtilsJS.getMCID(tex);
		hoverTexture = texture;
	}

	public void setHoverTexture(String tex)
	{
		hoverTexture = UtilsJS.getMCID(tex);
	}

	@Override
	public void renderBackground(MatrixStack matrixStack, float partialTicks)
	{
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(getUi().getWidgetTexture());
		RenderSystem.color4f(255, 255, 255, alpha);
		int i;
		int j;

		if (!enabled || getAction() == null)
		{
			i = 0;
			j = 0xCCCCCC;
		}
		else if (isMouseOver)
		{
			i = 2;
			j = hoverColor;
		}
		else
		{
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
		RenderSystem.color4f(255, 255, 255, 255);

		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Minecraft.getInstance().getTextureManager().bind(isMouseOver ? hoverTexture : texture);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuilder();
		Matrix4f m = matrixStack.last().pose();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
		builder.vertex(m, 1F, h - 1F, 0F).color(cr, cg, cb, ca).uv(0F, 1F).endVertex();
		builder.vertex(m, w - 1F, h - 1F, 0F).color(cr, cg, cb, ca).uv(1F, 1F).endVertex();
		builder.vertex(m, w - 1F, 1F, 0F).color(cr, cg, cb, ca).uv(1F, 0F).endVertex();
		builder.vertex(m, 1F, 1F, 0F).color(cr, cg, cb, ca).uv(0F, 0F).endVertex();
		tessellator.end();
		matrixStack.popPose();
	}
}