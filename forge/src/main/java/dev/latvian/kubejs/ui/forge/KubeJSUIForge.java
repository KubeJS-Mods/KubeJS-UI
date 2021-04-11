package dev.latvian.kubejs.ui.forge;

import dev.latvian.kubejs.ui.KubeJSUI;
import net.minecraftforge.fml.common.Mod;

@Mod(KubeJSUI.MOD_ID)
public class KubeJSUIForge {
	public KubeJSUIForge() {
		KubeJSUI.init();
	}
}
