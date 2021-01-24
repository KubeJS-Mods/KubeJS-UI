package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.KubeJS;
import me.shedaniel.architectury.registry.ReloadListeners;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;
import net.minecraft.server.packs.PackType;

import static net.minecraft.world.InteractionResultHolder.*;

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
		KubeJS.LOGGER.warn("thing");
		EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
			SetScreenEvent.EVENT.register(gui -> pass(KubeJSUIEventHandler.openGui(gui)));
			ReloadListeners.registerReloadListener(PackType.CLIENT_RESOURCES, UIData.INSTANCE);
		});
	}
}