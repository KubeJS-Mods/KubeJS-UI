package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class Panel extends Widget
{
	public final List<Widget> children = new ArrayList<>();

	@Override
	public void collectWidgets(List<Widget> list)
	{
		super.collectWidgets(list);

		for (Widget w : children)
		{
			w.collectWidgets(list);
		}
	}

	public <W extends Widget> void widget(Supplier<W> s, Consumer<W> c)
	{
		W w = s.get();
		w.parent = this;
		children.add(w);
		c.accept(w);
	}

	public void panel(Consumer<Panel> c)
	{
		widget(Panel::new, c);
	}

	public void button(Consumer<Button> c)
	{
		widget(Button::new, c);
	}

	public void imageButton(Consumer<ImageButton> c)
	{
		widget(ImageButton::new, c);
	}

	public void label(Consumer<Label> c)
	{
		widget(Label::new, c);
	}

	public void image(Consumer<Image> c)
	{
		widget(Image::new, c);
	}

	public void background(String texture)
	{
		image(image -> {
			image.setTexture(texture);
			image.setX(0);
			image.setY(0);
			image.setWidth(getWidth());
			image.setHeight(getHeight());
		});
	}

	public void tilingBackground(String texture, int size)
	{
		image(image -> {
			image.setTexture(texture);
			image.setTileSize(size);
			image.setX(0);
			image.setY(0);
			image.setWidth(getWidth());
			image.setHeight(getHeight());
		});
	}

	public void minecraftLogo(int x, int y)
	{
		image(i -> {
			i.setTexture("minecraft:textures/gui/title/minecraft.png");
			i.setX(x);
			i.setY(y);
			i.setWidth(155);
			i.setHeight(44);
			i.setUv(new int[] {0, 0, 155, 44});
		});

		image(i -> {
			i.setTexture("minecraft:textures/gui/title/minecraft.png");
			i.setX(x + 155);
			i.setY(y);
			i.setWidth(155);
			i.setHeight(44);
			i.setUv(new int[] {0, 45, 155, 44});
		});
	}

	public void minecraftLogo(int y)
	{
		minecraftLogo(getWidth() / 2 - 137, y);
	}

	@Override
	public void renderBackground(MatrixStack matrixStack)
	{
		for (Widget w : children)
		{
			w.renderBackground(matrixStack);
		}
	}

	@Override
	public void renderForeground(MatrixStack matrixStack)
	{
		for (Widget w : children)
		{
			w.renderForeground(matrixStack);
		}
	}

	@Override
	public boolean mousePressed()
	{
		for (Widget w : children)
		{
			if (w.mousePressed())
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void mouseReleased()
	{
		for (Widget w : children)
		{
			w.mouseReleased();
		}
	}
}
