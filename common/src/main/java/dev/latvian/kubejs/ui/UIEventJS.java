package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.ui.widget.UI;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class UIEventJS extends EventJS {
	public Consumer<UI> consumer;
	public int forcedScale = -1;

	@Override
	public boolean canCancel() {
		return true;
	}

	public void replace(Consumer<UI> c) {
		consumer = c;
		cancel();
	}
}