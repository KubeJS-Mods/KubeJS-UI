package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

/**
 * @author LatvianModder
 */
public class Label extends Widget {
	private Component cachedComponentUnderlined;
	public boolean shadow;

	public Label() {
		z = 20;
		setW(1);
		setH(10);
		shadow = false;
	}

	@Override
	public void setName(Component name) {
		super.setName(name);
		cachedComponentUnderlined = new TextComponent("").append(this.name).withStyle(ChatFormatting.UNDERLINE);
		setW((int) (getUi().screen.getUiFont().width(this.name) * (getH() / 10F)));
	}

	@Override
	public void renderForeground(PoseStack matrixStack, float partialTicks) {
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		matrixStack.pushPose();
		matrixStack.translate(actualX, actualY, z);

		int h = getH();
		matrixStack.scale(h / 10F, h / 10F, 1F);

		Component c = (isMouseOver && getAction() != null) ? cachedComponentUnderlined : name;
		int col = (isMouseOver ? hoverColor : color) | (alpha << 24);

		if (shadow) {
			getUi().screen.getUiFont().drawShadow(matrixStack, c, 0, 0, col);
		} else {
			getUi().screen.getUiFont().draw(matrixStack, c, 0, 0, col);
		}

		matrixStack.popPose();
	}
}
