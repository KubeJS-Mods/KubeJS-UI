package dev.latvian.kubejs.ui.fabric;

import com.google.gson.JsonElement;
import me.shedaniel.architectury.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;

public class UIDataImpl {
	public static void addMappedScreen(Map<Class<?>, String> screenIds, Map.Entry<String, JsonElement> entry) {
		if (FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace().equals("intermediary")) {
			return;
		}
		try {
			String className = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", entry.getKey());
			screenIds.put(Class.forName(className), entry.getValue().getAsString());
		} catch (Throwable ex) {
			// KubeJS.LOGGER.error("UI: Failed to load screen " + entry.getKey() + ":" + entry.getValue() + ": " + ex);
		}
	}

	public static void registerPlatformScreensAndActions(Map<Class<?>, String> screenIds, Map<ResourceLocation, Consumer<Screen>> actions) {
		if (Platform.isModLoaded("modmenu")) {
			actions.put(new ResourceLocation("fabric:mod_list"), FabricActions.FABRIC_MOD_LIST);
		}
	}
}
