package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * @author LatvianModder
 */
public class Label extends Widget
{
	private ITextComponent cachedComponentUnderlined;
	public boolean shadow;

	public Label()
	{
		z = 20;
		setWidth(1);
		setHeight(10);
		shadow = false;
	}

	@Override
	public void setName(Object o)
	{
		super.setName(o);
		cachedComponentUnderlined = new StringTextComponent("").append(cachedComponent).withStyle(TextFormatting.UNDERLINE);
		setWidth((int) (getUi().screen.getUiFont().width(cachedComponent) * (getHeight() / 10F)));
	}

	@Override
	public void renderForeground(MatrixStack matrixStack, float partialTicks)
	{
		RenderSystem.color4f(255, 255, 255, 255);
		matrixStack.pushPose();
		matrixStack.translate(actualX, actualY, z);

		int h = getHeight();

		if (h > 10)
		{
			matrixStack.scale(h / 10F, h / 10F, 1F);
		}

		ITextComponent c = (isMouseOver && getAction() != null) ? cachedComponentUnderlined : cachedComponent;
		int col = (isMouseOver ? hoverColor : color) | (alpha << 24);

		if (shadow)
		{
			getUi().screen.getUiFont().drawShadow(matrixStack, c, 0, 0, col);
		}
		else
		{
			getUi().screen.getUiFont().draw(matrixStack, c, 0, 0, col);
		}

		matrixStack.popPose();
	}
}
