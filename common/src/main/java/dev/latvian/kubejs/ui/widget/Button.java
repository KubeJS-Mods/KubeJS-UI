package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.text.Text;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.FormattedCharSequence;

/**
 * @author LatvianModder
 */
public class Button extends Widget {
	public boolean shadow;

	public Button() {
		z = 10;
		setWidth(200);
		setHeight(20);
		shadow = true;
	}

	@Override
	public void setName(Text o) {
		super.setName(o);
		setWidth(getUi().screen.getUiFont().width(cachedComponent) + 10);
	}

	@Override
	public void renderBackground(PoseStack matrixStack, float partialTicks) {
		Font fontrenderer = getUi().screen.getUiFont();
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

		if (shadow) {
			drawCenteredString(matrixStack, fontrenderer, cachedComponent, w / 2, (h - 8) / 2, j | (alpha << 24));
		} else {
			// RenderSystem.enableAlphaTest();
			FormattedCharSequence ireorderingprocessor = cachedComponent.getVisualOrderText();
			fontrenderer.draw(matrixStack, ireorderingprocessor, (w - fontrenderer.width(ireorderingprocessor)) / 2F, (h - 8) / 2F, j | (alpha << 24));
		}

		matrixStack.popPose();
	}
}