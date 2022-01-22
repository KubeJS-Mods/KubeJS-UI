package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.kubejs.ui.ScreenKubeJSUI;
import dev.latvian.kubejs.ui.UIData;
import dev.latvian.kubejs.ui.UIEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.text.Text;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class Widget extends GuiComponent {
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
	public List<Component> hoverTextComponents;

	protected Component cachedComponent;
	public int actualX, actualY;
	public boolean isMouseOver;

	public Widget() {
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

		cachedComponent = TextComponent.EMPTY;
		actualX = 0;
		actualY = 0;
		isMouseOver = false;
	}

	public UI getUi() {
		return parent.getUi();
	}

	public int getX() {
		return x + parent.getX();
	}

	public void setX(int _x) {
		x = _x;
	}

	public final void setX(double _x) {
		setX(Mth.floor(_x));
	}

	public int getY() {
		return y + parent.getY();
	}

	public void setY(int _y) {
		y = _y;
	}

	public final void setY(double _y) {
		setY(Mth.floor(_y));
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int w) {
		width = w;
	}

	public double getActualWidth() {
		return getWidth() / getUi().getScale();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int h) {
		height = h;
	}

	public double getActualHeight() {
		return getHeight() / getUi().getScale();
	}

	public Text getName() {
		return name;
	}

	public void setName(Text o) {
		name = o;
		cachedComponent = name.component();
	}

	@Nullable
	public Consumer<Screen> getAction() {
		return action;
	}

	public void setAction(Consumer<Screen> r) {
		action = r;
	}

	public void setAction(String id) {
		if (id.startsWith("$")) {
			setAction(s -> {
				try {
					UIEventJS e = new UIEventJS();

					if (e.post(ScriptType.CLIENT, "ui." + id) && e.consumer != null) {
						Minecraft.getInstance().setScreen(new ScreenKubeJSUI(id, s, e.consumer, e.forcedScale));
					}
				} catch (Exception ex) {
					ScriptType.CLIENT.console.error("Failed to create " + id + " UI:");
					ex.printStackTrace();
				}
			});
		} else if (id.startsWith("http://") || id.startsWith("https://")) {
			setAction(screen -> getUi().screen.handleComponentClicked(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, id))));
		} else {
			setAction(UIData.INSTANCE.getAction(id));
		}
	}

	public void setClickSound(SoundEvent e) {
		clickSound = e;
	}

	public void setClickSound(String sound) {
		setClickSound(Registry.SOUND_EVENT.get(UtilsJS.getMCID(sound)));
	}

	public void collectWidgets(List<Widget> list) {
		list.add(this);
	}

	public void renderBackground(PoseStack matrixStack, float partialTicks) {
	}

	public void renderForeground(PoseStack matrixStack, float partialTicks) {
	}

	public boolean mousePressed() {
		if (isMouseOver && getAction() != null) {
			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(clickSound, 1F));
			getAction().accept(getUi().screen);
			return true;
		}

		return false;
	}

	public void mouseReleased() {
	}

	public void setHoverText(Object[] array) {
		hoverTextComponents = new ArrayList<>(array.length);

		for (Object o : array) {
			hoverTextComponents.add(Text.of(o).component());
		}
	}

	public void setHoverText(Object o) {
		setHoverText(new Object[]{o});
	}

	public void appendHoverText(List<Component> list) {
		if (hoverTextComponents != null && isMouseOver) {
			list.addAll(hoverTextComponents);
		}
	}
}