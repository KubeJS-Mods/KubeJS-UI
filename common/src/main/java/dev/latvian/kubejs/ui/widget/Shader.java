package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import dev.latvian.kubejs.ui.KubeJSUIClient;
import net.minecraft.client.renderer.ShaderInstance;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class Shader extends Widget {
	public Shader() {
		setW(64);
		setH(64);
	}

	@Override
	public void renderBackground(PoseStack matrixStack, float partialTicks) {
		int w = getW();
		int h = getH();

		if (w <= 0 || h <= 0) {
			return;
		}

		ShaderInstance shaderInstance = KubeJSUIClient.getBackgroundShader();

		if (shaderInstance == null) {
			return;
		}

		UI ui = getUi();
		int sw = ui.screen.getMinecraft().getWindow().getWidth();
		int sh = ui.screen.getMinecraft().getWindow().getHeight();
		double scale = ui.getScale();

		RenderSystem.setShader(KubeJSUIClient::getBackgroundShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		if (KubeJSUIClient.RESOLUTION_UNIFORM != null) {
			KubeJSUIClient.RESOLUTION_UNIFORM.set((float) sw, (float) sh);
		}

		if (KubeJSUIClient.TIME_UNIFORM != null) {
			KubeJSUIClient.TIME_UNIFORM.set((float) (ui.time / 1000D));
		}

		if (KubeJSUIClient.TICK_UNIFORM != null) {
			KubeJSUIClient.TICK_UNIFORM.set(ui.tick - 1F + partialTicks);
		}

		if (KubeJSUIClient.MOUSE_UNIFORM != null) {
			KubeJSUIClient.MOUSE_UNIFORM.set((float) (ui.mouse.x * scale / (double) sw), (float) ((h - ui.mouse.y) * scale / (double) sh));
		}

		if (KubeJSUIClient.GUI_SCALE_UNIFORM != null) {
			KubeJSUIClient.GUI_SCALE_UNIFORM.set((float) scale);
		}

		RenderSystem.depthFunc(GL11.GL_ALWAYS);

		Tesselator t = Tesselator.getInstance();
		BufferBuilder builder = t.getBuilder();
		Matrix4f m = matrixStack.last().pose();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		builder.vertex(m, actualX, actualY + h, z).endVertex();
		builder.vertex(m, actualX + w, actualY + h, z).endVertex();
		builder.vertex(m, actualX + w, actualY, z).endVertex();
		builder.vertex(m, actualX, actualY, z).endVertex();
		t.end();

		RenderSystem.depthFunc(GL11.GL_LEQUAL);
		// program.clear();

		RenderSystem.enableTexture();
	}
}