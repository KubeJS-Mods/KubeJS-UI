package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.script.ScriptType;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResultHolder;

import static net.minecraft.world.InteractionResultHolder.pass;
import static net.minecraft.world.InteractionResultHolder.success;

/**
 * @author LatvianModder
 */
public class KubeJSUIEventHandler {
	public static InteractionResultHolder<Screen> openGui(Screen screen) {
		if (screen == null) {
			return pass(screen);
		} else if (screen instanceof ScreenKubeJSUI) {
			ScreenKubeJSUI o = (ScreenKubeJSUI) screen;

			try {
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + o.screenId) && e.consumer != null) {
					return success(new ScreenKubeJSUI(o.screenId, o.original, e.consumer, e.forcedScale));
				}
			} catch (Exception ex) {
				ScriptType.CLIENT.console.error("Failed to create " + o.screenId + " UI:");
				ex.printStackTrace();
			}

			return pass(screen);
		}

		String id = UIData.INSTANCE.getScreenId(screen.getClass());

		if (!id.isEmpty()) {
			try {
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + id) && e.consumer != null) {
					return success(new ScreenKubeJSUI(id, screen, e.consumer, e.forcedScale));
				}
			} catch (Exception ex) {
				ScriptType.CLIENT.console.error("Failed to create " + id + " UI:");
				ex.printStackTrace();
			}
		}

		return pass(screen);
	}
}