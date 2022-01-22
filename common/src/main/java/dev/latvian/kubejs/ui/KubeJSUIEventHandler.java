package dev.latvian.kubejs.ui;

import dev.architectury.event.CompoundEventResult;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.client.gui.screens.Screen;

/**
 * @author LatvianModder
 */
public class KubeJSUIEventHandler {
	public static CompoundEventResult<Screen> openGui(Screen screen) {
		if (screen == null) {
			return CompoundEventResult.pass();
		} else if (screen instanceof ScreenKubeJSUI) {
			ScreenKubeJSUI o = (ScreenKubeJSUI) screen;

			try {
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + o.screenId) && e.consumer != null) {
					return CompoundEventResult.interruptTrue(new ScreenKubeJSUI(o.screenId, o.original, e.consumer, e.forcedScale));
				}
			} catch (Exception ex) {
				ScriptType.CLIENT.console.error("Failed to create " + o.screenId + " UI:");
				ex.printStackTrace();
			}

			return CompoundEventResult.pass();
		}

		String id = UIData.INSTANCE.getScreenId(screen.getClass());

		if (!id.isEmpty()) {
			try {
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + id) && e.consumer != null) {
					return CompoundEventResult.interruptTrue(new ScreenKubeJSUI(id, screen, e.consumer, e.forcedScale));
				}
			} catch (Exception ex) {
				ScriptType.CLIENT.console.error("Failed to create " + id + " UI:");
				ex.printStackTrace();
			}
		}

		return CompoundEventResult.pass();
	}
}