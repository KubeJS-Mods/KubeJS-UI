package dev.latvian.kubejs.ui;

import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.ReloadListeners;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionResult;

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
			GuiEvent.SET_SCREEN.register(KubeJSUIEventHandler::openGui);
			if (!Platform.isForge()) {
				GuiEvent.INIT_PRE.register((screen, widgets, children) -> {
					if (withinInitPreHacks.get()) {
						return InteractionResult.PASS;
					}
					if (Minecraft.getInstance().screen != screen) {
						return InteractionResult.PASS;
					}
					if (screen instanceof ScreenKubeJSUI) {
						return InteractionResult.PASS;
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
						return InteractionResult.SUCCESS;
					}

					return InteractionResult.PASS;
				});
			}
			ReloadListeners.registerReloadListener(PackType.CLIENT_RESOURCES, UIData.INSTANCE);
		});
	}
}