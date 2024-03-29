package dev.latvian.kubejs.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public interface VanillaActions {
	static Minecraft mc() {
		return Minecraft.getInstance();
	}

	Consumer<Screen> SINGLEPLAYER = screen -> mc().setScreen(new SelectWorldScreen(screen));
	Consumer<Screen> MULTIPLAYER = screen -> mc().setScreen(mc().options.skipMultiplayerWarning ? new JoinMultiplayerScreen(screen) : new SafetyScreen(screen));
	Consumer<Screen> LANGUAGE = screen -> mc().setScreen(new LanguageSelectScreen(screen, mc().options, mc().getLanguageManager()));
	Consumer<Screen> OPTIONS = screen -> mc().setScreen(new OptionsScreen(screen, mc().options));
	Consumer<Screen> QUIT = screen -> mc().stop();
	Consumer<Screen> ACCESSIBILITY = screen -> mc().setScreen(new AccessibilityOptionsScreen(screen, mc().options));

	Consumer<Screen> EXAMPLE = screen -> mc().setScreen(new AccessibilityOptionsScreen(screen, mc().options));

	Consumer<Screen> KUBEJSUI_TOGGLE_SHADERS = screen -> {
		KubeJSUIOptions.getInstance().useShaders = !KubeJSUIOptions.getInstance().useShaders;
		KubeJSUIOptions.getInstance().save();
		mc().setScreen(screen);
	};
}
