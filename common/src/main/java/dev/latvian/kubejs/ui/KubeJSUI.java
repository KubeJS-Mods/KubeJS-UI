package dev.latvian.kubejs.ui;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.bindings.event.ClientEvents;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.event.EventHandler;

/**
 * @author LatvianModder
 */
public class KubeJSUI {
	public static final String MOD_ID = "kubejs_ui";

	private KubeJSUI() {
	}

	public static void init() {
		EnvExecutor.runInEnv(Env.CLIENT, () -> KubeJSUIClient::init);
	}
}
