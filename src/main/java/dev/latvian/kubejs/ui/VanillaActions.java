package dev.latvian.kubejs.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraftforge.fml.client.gui.screen.ModListScreen;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public interface VanillaActions
{
	static Minecraft mc()
	{
		return Minecraft.getInstance();
	}

	Consumer<Screen> SINGLEPLAYER = screen -> mc().setScreen(new WorldSelectionScreen(screen));
	Consumer<Screen> MULTIPLAYER = screen -> mc().setScreen(mc().options.skipMultiplayerWarning ? new MultiplayerScreen(screen) : new MultiplayerWarningScreen(screen));
	Consumer<Screen> REALMS = screen -> new RealmsBridgeScreen().switchToRealms(screen);
	Consumer<Screen> LANGUAGE = screen -> mc().setScreen(new LanguageScreen(screen, mc().options, mc().getLanguageManager()));
	Consumer<Screen> OPTIONS = screen -> mc().setScreen(new OptionsScreen(screen, mc().options));
	Consumer<Screen> QUIT = screen -> mc().stop();
	Consumer<Screen> ACCESSIBILITY = screen -> mc().setScreen(new AccessibilityScreen(screen, mc().options));

	Consumer<Screen> FORGE_MOD_LIST = screen -> mc().setScreen(new ModListScreen(screen));

	Consumer<Screen> KUBEJSUI_TOGGLE_SHADERS = screen -> {
		KubeJSUIOptions.getInstance().useShaders = !KubeJSUIOptions.getInstance().useShaders;
		KubeJSUIOptions.getInstance().save();
		mc().setScreen(screen);
	};
}
