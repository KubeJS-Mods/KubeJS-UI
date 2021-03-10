package dev.latvian.kubejs.ui.fabric;

import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Consumer;

import static dev.latvian.kubejs.ui.VanillaActions.*;

public interface FabricActions
{
	Consumer<Screen> FABRIC_MOD_LIST = screen -> {
		if (Platform.isModLoaded("modmenu"))
		{
			ModMenuApi.createModsScreen(screen);
		}
	};
}
