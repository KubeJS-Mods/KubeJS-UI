package dev.latvian.kubejs.ui;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;

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