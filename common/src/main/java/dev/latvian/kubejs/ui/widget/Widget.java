package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.kubejs.ui.ScreenKubeJSUI;
import dev.latvian.kubejs.ui.UIData;
import dev.latvian.kubejs.ui.UIEventJS;
import dev.latvian.mods.kubejs.bindings.ComponentWrapper;
import dev.latvian.mods.kubejs.script.ScriptType;
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
	private int x, y, w, h;
	public int z;
	protected Component name;
	private Consumer<Screen> action;
	public int alpha;
	public boolean enabled;
	private SoundEvent clickSound;
	public Runnable mouseEnter;
	public Runnable mouseExit;
	public int color;
	public int hoverColor;
	public List<Component> hoverTextComponents;

	public int actualX, actualY;
	public boolean isMouseOver;

	public Widget() {
		x = 0;
		y = 0;
		w = 16;
		h = 16;
		z = 0;
		name = TextComponent.EMPTY;
		action = null;
		alpha = 255;
		enabled = true;
		clickSound = SoundEvents.UI_BUTTON_CLICK;
		mouseEnter = null;
		mouseExit = null;
		color = 0xFFFFFF;
		hoverColor = 0xFFFFFF;

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

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public double getActualW() {
		return getW() / getUi().getScale();
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public double getActualH() {
		return getH() / getUi().getScale();
	}

	public Component getName() {
		return name;
	}

	public void setName(Component name) {
		this.name = name;
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

	public void setHoverComponent(Component[] array) {
		hoverTextComponents = new ArrayList<>(array.length);

		for (Object o : array) {
			hoverTextComponents.add(ComponentWrapper.of(o));
		}
	}

	public void appendHoverText(List<Component> list) {
		if (hoverTextComponents != null && isMouseOver) {
			list.addAll(hoverTextComponents);
		}
	}

	@Deprecated
	public final int getWidth() {
		return getW();
	}

	@Deprecated
	public final int getHeight() {
		return getH();
	}

	@Deprecated
	public final void setWidth(int w) {
		setW(w);
	}

	@Deprecated
	public final void setHeight(int h) {
		setH(h);
	}
}