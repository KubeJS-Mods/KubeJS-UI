package dev.latvian.kubejs.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.latvian.kubejs.ui.widget.UI;
import dev.latvian.kubejs.ui.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public final class ScreenKubeJSUI extends Screen
{
	public final String screenId;
	public final Screen original;
	public final Consumer<UI> consumer;
	public final UI ui;

	public ScreenKubeJSUI(String i, Screen o, Consumer<UI> c)
	{
		super(o.getTitle());
		screenId = i;
		original = o;
		consumer = c;
		ui = new UI(this);
	}

	@Override
	public void init(Minecraft mc, int w, int h)
	{
		original.init(mc, w, h);
		super.init(mc, w, h);
	}

	@Override
	public void init()
	{
		ui.children.clear();
		ui.allWidgets.clear();
		consumer.accept(ui);
		ui.collectWidgets(ui.allWidgets);

		for (Widget w : ui.allWidgets)
		{
			w.actualX = w.getX();
			w.actualY = w.getY();
		}
	}

	public FontRenderer getUiFont()
	{
		return font;
	}

	@Override
	public boolean shouldCloseOnEsc()
	{
		return original.shouldCloseOnEsc();
	}

	@Override
	public boolean isPauseScreen()
	{
		return original.isPauseScreen();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		original.renderBackground(matrixStack);
		ui.mouse.x = mouseX;
		ui.mouse.y = mouseY;

		for (Widget w : ui.allWidgets)
		{
			boolean b = w.isMouseOver;
			w.isMouseOver = mouseX >= w.actualX && mouseY >= w.actualY && mouseX < w.actualX + w.getWidth() && mouseY < w.actualY + w.getHeight();

			if (b != w.isMouseOver)
			{
				if (w.isMouseOver)
				{
					if (w.mouseEnter != null)
					{
						w.mouseEnter.run();
					}
				}
				else if (w.mouseExit != null)
				{
					w.mouseExit.run();
				}
			}
		}

		ui.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		ui.renderForeground(matrixStack);
	}

	@Override
	public boolean mouseClicked(double x, double y, int button)
	{
		if (ui.mousePressed())
		{
			return true;
		}

		return super.mouseClicked(x, y, button);
	}

	@Override
	public boolean mouseReleased(double x, double y, int button)
	{
		ui.mouseReleased();
		return super.mouseReleased(x, y, button);
	}
}