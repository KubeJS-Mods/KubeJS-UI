package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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

		UI ui = getUi();

		int sw = ui.screen.getMinecraft().getWindow().getWidth();
		int sh = ui.screen.getMinecraft().getWindow().getHeight();

		RenderSystem.color4f(255, 255, 255, 255);
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		program.safeGetUniform("resolution").set(sw, sh);
		program.safeGetUniform("time").set(ui.time / 1000F);
		program.safeGetUniform("mouse").set(ui.mouse.x / (float) sw, (ui.mouse.y / (float) sh));
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
	}
}