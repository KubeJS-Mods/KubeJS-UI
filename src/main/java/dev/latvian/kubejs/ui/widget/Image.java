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
public class Image extends Widget
{
	private ResourceLocation texture;
	private ResourceLocation hoverTexture;
	private int[] uv;
	private int tileSize;

	public Image()
	{
		setWidth(64);
		setHeight(64);
		setTexture("minecraft:textures/gui/presets/isles.png");
		uv = null;
		tileSize = 0;
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

	public void setUv(int[] ai)
	{
		uv = ai;
	}

	public void setTileSize(int s)
	{
		tileSize = s;
	}

	@Override
	public void renderBackground(MatrixStack matrixStack)
	{
		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Minecraft.getInstance().getTextureManager().bind(isMouseOver ? hoverTexture : texture);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuilder();

		int col = isMouseOver ? hoverColor : color;
		int cr = (col >> 16) & 255;
		int cg = (col >> 8) & 255;
		int cb = (col >> 0) & 255;
		int ca = alpha;

		int w = getWidth();
		int h = getHeight();

		matrixStack.pushPose();
		matrixStack.translate(actualX, actualY, z);
		matrixStack.scale(w, h, 1F);
		Matrix4f m = matrixStack.last().pose();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

		if (tileSize != 0)
		{
			float s = tileSize < 0 ? -tileSize : (float) (tileSize / Minecraft.getInstance().getWindow().getGuiScale());
			builder.vertex(m, 0F, 1F, 0F).color(cr, cg, cb, ca).uv(0F, h / s).endVertex();
			builder.vertex(m, 1F, 1F, 0F).color(cr, cg, cb, ca).uv(w / s, h / s).endVertex();
			builder.vertex(m, 1F, 0F, 0F).color(cr, cg, cb, ca).uv(w / s, 0F).endVertex();
			builder.vertex(m, 0F, 0F, 0F).color(cr, cg, cb, ca).uv(0F, 0F).endVertex();
		}
		else
		{
			int[] uv1 = {0, 0, 256, 256, 256, 256};

			if (uv != null)
			{
				System.arraycopy(uv, 0, uv1, 0, Math.min(uv.length, 6));
			}

			float u0 = uv1[0] / (float) uv1[4];
			float v0 = uv1[1] / (float) uv1[5];
			float u1 = (uv1[0] + uv1[2]) / (float) uv1[4];
			float v1 = (uv1[1] + uv1[3]) / (float) uv1[5];

			builder.vertex(m, 0F, 1F, 0F).color(cr, cg, cb, ca).uv(u0, v1).endVertex();
			builder.vertex(m, 1F, 1F, 0F).color(cr, cg, cb, ca).uv(u1, v1).endVertex();
			builder.vertex(m, 1F, 0F, 0F).color(cr, cg, cb, ca).uv(u1, v0).endVertex();
			builder.vertex(m, 0F, 0F, 0F).color(cr, cg, cb, ca).uv(u0, v0).endVertex();
		}

		tessellator.end();
		matrixStack.popPose();
	}
}