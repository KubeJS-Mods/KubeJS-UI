package dev.latvian.kubejs.ui.forge;

import com.google.gson.JsonElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;

public class UIDataImpl {
	public static void addMappedScreen(Map<Class<?>, String> screenIds, Map.Entry<String, JsonElement> entry) {
	}

	public static void registerPlatformScreensAndActions(Map<Class<?>, String> screenIds, Map<ResourceLocation, Consumer<Screen>> actions) {
		actions.put(new ResourceLocation("forge:mod_list"), ForgeActions.FORGE_MOD_LIST);
	}
}
