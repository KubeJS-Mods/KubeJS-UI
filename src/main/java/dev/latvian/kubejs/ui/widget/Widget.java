package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.latvian.kubejs.text.Text;
import dev.latvian.kubejs.ui.UIData;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class Widget extends AbstractGui
{
	public Panel parent;
	private int x, y, width, height;
	public int z;
	private Text name;
	private Consumer<Screen> action;
	public int alpha;
	public boolean enabled;
	private SoundEvent clickSound;
	public Runnable mouseEnter;
	public Runnable mouseExit;
	public int color;
	public int hoverColor;

	protected ITextComponent cachedComponent;
	public int actualX, actualY;
	public boolean isMouseOver;

	public Widget()
	{
		x = 0;
		y = 0;
		width = 16;
		height = 16;
		z = 0;
		name = Text.of("");
		action = null;
		alpha = 255;
		enabled = true;
		clickSound = SoundEvents.UI_BUTTON_CLICK;
		mouseEnter = null;
		mouseExit = null;
		color = 0xFFFFFF;
		hoverColor = 0xFFFFFF;

		cachedComponent = StringTextComponent.EMPTY;
		actualX = 0;
		actualY = 0;
		isMouseOver = false;
	}

	public UI getUi()
	{
		return parent.getUi();
	}

	public int getX()
	{
		return x + parent.getX();
	}

	public void setX(int _x)
	{
		x = _x;
	}

	public final void setX(double _x)
	{
		setX(MathHelper.floor(_x));
	}

	public int getY()
	{
		return y + parent.getY();
	}

	public void setY(int _y)
	{
		y = _y;
	}

	public final void setY(double _y)
	{
		setY(MathHelper.floor(_y));
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int w)
	{
		width = w;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int h)
	{
		height = h;
	}

	public Text getName()
	{
		return name;
	}

	public void setName(Object o)
	{
		name = Text.of(o);
		cachedComponent = name.component();
	}

	@Nullable
	public Consumer<Screen> getAction()
	{
		return action;
	}

	public void setAction(Consumer<Screen> r)
	{
		action = r;
	}

	public void setAction(String id)
	{
		if (id.startsWith("http://") || id.startsWith("https://"))
		{
			setAction(screen -> getUi().screen.handleComponentClicked(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, id))));
		}
		else
		{
			setAction(UIData.get().getAction(id));
		}
	}

	public void setClickSound(SoundEvent e)
	{
		clickSound = e;
	}

	public void setClickSound(String sound)
	{
		setClickSound(ForgeRegistries.SOUND_EVENTS.getValue(UtilsJS.getMCID(sound)));
	}

	public void collectWidgets(List<Widget> list)
	{
		list.add(this);
	}

	public void renderBackground(MatrixStack matrixStack)
	{
	}

	public void renderForeground(MatrixStack matrixStack)
	{
	}

	public boolean mousePressed()
	{
		if (isMouseOver && getAction() != null)
		{
			Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(clickSound, 1F));
			getAction().accept(getUi().screen);
			return true;
		}

		return false;
	}

	public void mouseReleased()
	{
	}
}