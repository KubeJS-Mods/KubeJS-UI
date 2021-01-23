package dev.latvian.kubejs.ui;

import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;
import net.minecraft.world.InteractionResult;

/**
 * @author LatvianModder
 */
public class KubeJSUI
{
	public static final String MOD_ID = "kubejs_ui";

	public KubeJSUI()
	{
		EnvExecutor.runInEnv(Env.CLIENT, () -> () -> DummyGuiOpenEvent.EVENT.register((event) -> {
			KubeJSUIEventHandler.openGui(event);
			return InteractionResult.PASS;
		}));
	}
}