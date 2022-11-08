package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.ui.widget.UI;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class UIEventJS extends EventJS {
	private Consumer<UI> consumer;
	private int forcedScale = -1;

	public UIEventJS() {
	}

	public void replace(Consumer<UI> c) {
		consumer = c;
		cancel();
	}

	public Consumer<UI> getConsumer() {
		return consumer;
	}

	public int getForcedScale() {
		return forcedScale;
	}
}
