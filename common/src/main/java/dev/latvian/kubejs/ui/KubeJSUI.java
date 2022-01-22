package dev.latvian.kubejs.ui;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;

/**
 * @author LatvianModder
 */
public class KubeJSUI {
	public static final String MOD_ID = "kubejs_ui";
	static ThreadLocal<Boolean> withinInitPreHacks = ThreadLocal.withInitial(() -> false);

	private KubeJSUI() {
	}

	public static void init() {
		EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
			ClientGuiEvent.SET_SCREEN.register(KubeJSUIEventHandler::openGui);

			if (!Platform.isForge()) {
				ClientGuiEvent.INIT_PRE.register((screen, access) -> {
					if (withinInitPreHacks.get()) {
						return EventResult.pass();
					}
					if (Minecraft.getInstance().screen != screen) {
						return EventResult.pass();
					}
					if (screen instanceof ScreenKubeJSUI) {
						return EventResult.pass();
					}

					String screenId = UIData.INSTANCE.getScreenId(screen.getClass());

					if (!screenId.isEmpty()) {
						// Re-open the screen
						withinInitPreHacks.set(true);
						try {
							Minecraft.getInstance().setScreen(screen);
						} finally {
							withinInitPreHacks.set(false);
						}
						return EventResult.interruptTrue();
					}

					return EventResult.pass();
				});
			}

			ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, UIData.INSTANCE);
		});
	}
}