package dev.latvian.kubejs.ui.widget;

import dev.latvian.kubejs.text.Text;
import dev.latvian.kubejs.ui.KubeJSUIOptions;
import dev.latvian.kubejs.ui.ScreenKubeJSUI;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

	public UI(ScreenKubeJSUI s) {
		screen = s;
		mouse = new Mouse();
		allWidgets = new ArrayList<>();
		widgetTexture = new ResourceLocation("minecraft:textures/gui/widgets.png");
		setName(Text.of(s.getTitle()));
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
	public int getWidth() {
		return screen.width;
	}

	@Override
	public int getHeight() {
		return screen.height;
	}

	public double getScale() {
		return screen.getMinecraft().getWindow().getGuiScale();
	}

	// Utils //

	public int textWidth(@Nullable Object o) {
		if (o == null) {
			return 0;
		} else if (o instanceof FormattedText) {
			return screen.getUiFont().width((FormattedText) o);
		} else if (o instanceof FormattedCharSequence) {
			return screen.getUiFont().width((FormattedCharSequence) o);
		} else if (o instanceof Text) {
			return screen.getUiFont().width(((Text) o).component());
		}

		return screen.getUiFont().width(o.toString());
	}

	public boolean getUseShaders() {
		return KubeJSUIOptions.getInstance().useShaders;
	}
}
