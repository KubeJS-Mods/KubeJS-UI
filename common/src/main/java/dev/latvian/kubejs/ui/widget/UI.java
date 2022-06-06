package dev.latvian.kubejs.ui.widget;

import dev.latvian.kubejs.ui.KubeJSUIOptions;
import dev.latvian.kubejs.ui.ScreenKubeJSUI;
import dev.latvian.mods.kubejs.bindings.ComponentWrapper;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

/**
 * @author LatvianModder
 */
public class UI extends Panel {
	public final ScreenKubeJSUI screen;
	public final Mouse mouse;
	public final List<Widget> allWidgets;
	public ResourceLocation widgetTexture;
	public int tick;
	public long time;
	public static long startTime;
	public boolean hasShader;
	public Runnable screenTick;
	public DoubleConsumer screenRender;

	public UI(ScreenKubeJSUI s) {
		screen = s;
		mouse = new Mouse();
		allWidgets = new ArrayList<>();
		widgetTexture = new ResourceLocation("minecraft:textures/gui/widgets.png");
		setName(ComponentWrapper.of(s.getTitle()));
		tick = 0;

		long now = System.currentTimeMillis();

		if (startTime == 0L) {
			startTime = now;
		}

		time = now - startTime;
	}

	@Override
	public UI getUi() {
		return this;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getW() {
		return screen.width;
	}

	@Override
	public int getH() {
		return screen.height;
	}

	public double getScale() {
		return screen.getMinecraft().getWindow().getGuiScale();
	}

	// Utils //

	public int textWidth(@Nullable Object o) {
		if (o == null) {
			return 0;
		} else if (o instanceof FormattedText ft) {
			return screen.getUiFont().width(ft);
		} else if (o instanceof FormattedCharSequence fcs) {
			return screen.getUiFont().width(fcs);
		}

		return screen.getUiFont().width(ComponentWrapper.of(o));
	}

	public boolean getUseShaders() {
		return KubeJSUIOptions.getInstance().useShaders;
	}
}
