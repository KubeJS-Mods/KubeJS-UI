package dev.latvian.kubejs.ui.fabric;

import dev.architectury.platform.Platform;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Consumer;

public interface FabricActions {
	Consumer<Screen> FABRIC_MOD_LIST = screen -> {
		if (Platform.isModLoaded("modmenu")) {
			ModMenuIntegration.openMenu(screen);
		}
	};
}
