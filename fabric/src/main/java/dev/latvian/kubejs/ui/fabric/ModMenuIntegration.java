package dev.latvian.kubejs.ui.fabric;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import static dev.latvian.kubejs.ui.VanillaActions.mc;

public class ModMenuIntegration {
	public static void openMenu(Screen screen) {
		mc().setScreen(ModMenuApi.createModsScreen(screen));
	}
}
