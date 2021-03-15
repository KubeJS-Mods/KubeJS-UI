package dev.latvian.kubejs.ui.fabric;

import com.google.gson.JsonElement;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Map;

public class UIDataImpl
{
	public static void addMappedScreen(Map<Class<?>, String> screenIds, Map.Entry<String, JsonElement> entry)
	{
		if (FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace().equals("intermediary")) return;
		try
		{
			String className = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", entry.getKey());
			screenIds.put(Class.forName(className), entry.getValue().getAsString());
		}
		catch (Throwable ex)
		{
			// KubeJS.LOGGER.error("UI: Failed to load screen " + entry.getKey() + ":" + entry.getValue() + ": " + ex);
		}
	}
}
