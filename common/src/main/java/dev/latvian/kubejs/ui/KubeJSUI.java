package dev.latvian.kubejs.ui;

import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.registry.ReloadListeners;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;
import net.minecraft.server.packs.PackType;

/**
 * @author LatvianModder
 */
public class KubeJSUI
{
	public static final String MOD_ID = "kubejs_ui";

	private KubeJSUI()
	{
	}

	public static void init()
	{
		EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
			GuiEvent.SET_SCREEN.register(KubeJSUIEventHandler::openGui);
			ReloadListeners.registerReloadListener(PackType.CLIENT_RESOURCES, UIData.INSTANCE);
		});
	}
}