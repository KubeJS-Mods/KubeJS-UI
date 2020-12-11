package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.ShaderInstance;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class Shader extends Widget
{
	public final ShaderInstance program;

	public Shader(@Nullable ShaderInstance s)
	{
		program = s;
		setWidth(64);
		setHeight(64);
	}

	@Override
	public void renderBackground(MatrixStack matrixStack, float partialTicks)
	{
		if (program == null)
		{
			return;
		}

		int w = getWidth();
		int h = getHeight();

		if (w <= 0 || h <= 0)
		{
			return;
		}

		int sw = Minecraft.getInstance().getWindow().getWidth();
		int sh = Minecraft.getInstance().getWindow().getHeight();

		RenderSystem.color4f(255, 255, 255, 255);
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		//RenderSystem.viewport(0, 0, w, h);

		//program.safeGetUniform("ProjMat").set(shaderOrthoMatrix);
		program.safeGetUniform("resolution").set(w, h);
		program.safeGetUniform("time").set((getUi().tick - 1F + partialTicks) / 20F);
		//program.safeGetUniform("ScreenSize").set((float) sw, (float) sh);
		program.apply();
		RenderSystem.depthFunc(GL11.GL_ALWAYS);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuilder();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		matrixStack.pushPose();
		matrixStack.translate(actualX - 1, actualY - 1, z);
		Matrix4f m = matrixStack.last().pose();
		builder.vertex(m, 0F, 0F, 0F).color(255, 255, 255, 255).endVertex();
		builder.vertex(m, w, 0F, 0F).color(255, 255, 255, 255).endVertex();
		builder.vertex(m, w, h, 0F).color(255, 255, 255, 255).endVertex();
		builder.vertex(m, 0F, h, 0F).color(255, 255, 255, 255).endVertex();
		tessellator.end();
		matrixStack.popPose();

		RenderSystem.depthFunc(GL11.GL_LEQUAL);
		program.clear();

		RenderSystem.enableTexture();
		//RenderSystem.viewport(0, 0, sw, sh);
	}
}