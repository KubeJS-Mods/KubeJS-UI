package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class Shader extends Widget {
	public final EffectInstance program;
	public final float scale;

	public Shader(@Nullable EffectInstance s, float sc) {
		program = s;
		scale = Math.max(sc, 1F);
		setWidth(64);
		setHeight(64);
	}

	@Override
	public void renderBackground(PoseStack matrixStack, float partialTicks) {
		if (program == null) {
			return;
		}

		int w = getWidth();
		int h = getHeight();

		if (w <= 0 || h <= 0) {
			return;
		}

		UI ui = getUi();

		int sw = ui.screen.getMinecraft().getWindow().getWidth();
		int sh = ui.screen.getMinecraft().getWindow().getHeight();

		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		program.safeGetUniform("ScreenSize").set(sw, sh);
		program.safeGetUniform("GameTime").set(RenderSystem.getShaderGameTime());
		program.safeGetUniform("Mouse").set(ui.mouse.x / (float) sw, (ui.mouse.y / (float) sh));
		program.apply();
		RenderSystem.depthFunc(GL11.GL_ALWAYS);

		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder builder = tessellator.getBuilder();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		matrixStack.pushPose();
		matrixStack.translate(actualX - 1, actualY - 1, z);
		Matrix4f m = matrixStack.last().pose();
		builder.vertex(m, 0F, 0F, 0F).color(255, 255, 255, 100).endVertex();
		builder.vertex(m, w, 0F, 0F).color(255, 255, 255, 100).endVertex();
		builder.vertex(m, w, h, 0F).color(255, 255, 255, 100).endVertex();
		builder.vertex(m, 0F, h, 0F).color(255, 255, 255, 100).endVertex();
		tessellator.end();
		matrixStack.popPose();

		RenderSystem.depthFunc(GL11.GL_LEQUAL);
		program.clear();

		RenderSystem.enableTexture();
	}
}