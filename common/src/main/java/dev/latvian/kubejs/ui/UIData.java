package dev.latvian.kubejs.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public enum UIData implements ResourceManagerReloadListener
{
	INSTANCE;

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{
		screenIds.clear();
		actions.clear();

		Gson gson = new GsonBuilder().create();

		for (String namespace : resourceManager.getNamespaces())
		{
			try
			{
				for (Resource resource : resourceManager.getResources(new ResourceLocation(namespace, "kubejsui.json")))
				{
					try (InputStream stream = resource.getInputStream())
					{
						JsonObject json = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);

						if (json.has("actions"))
						{
							for (Map.Entry<String, JsonElement> entry : json.get("actions").getAsJsonObject().entrySet())
							{
								String s = entry.getValue().getAsString();

								try
								{
									int i = s.lastIndexOf('.');
									Class<?> clazz = Class.forName(s.substring(0, i));
									Field field = clazz.getDeclaredField(s.substring(i + 1));
									field.setAccessible(true);
									Consumer<Screen> consumer = Objects.requireNonNull((Consumer<Screen>) field.get(null));
									actions.put(new ResourceLocation(namespace, entry.getKey()), consumer);
								}
								catch (Throwable ex)
								{
									KubeJS.LOGGER.error("UI: Failed to load action " + entry.getKey() + ":" + entry.getValue() + ": " + ex);
								}
							}
						}

						if (json.has("screens"))
						{
							for (Map.Entry<String, JsonElement> entry : json.get("screens").getAsJsonObject().entrySet())
							{
								try
								{
									Class<?> clazz = Class.forName(entry.getKey());
									screenIds.put(clazz, entry.getValue().getAsString());
								}
								catch (Throwable ex)
								{
									KubeJS.LOGGER.error("UI: Failed to load screen " + entry.getKey() + ":" + entry.getValue() + ": " + ex);
								}
							}
						}
					}
				}
			}
			catch (Exception ignored)
			{
			}
		}
	}

	private final Map<Class<?>, String> screenIds = new HashMap<>();
	private final Map<ResourceLocation, Consumer<Screen>> actions = new HashMap<>();

	@Nullable
	public Consumer<Screen> getAction(String id)
	{
		return actions.get(UtilsJS.getMCID(id));
	}

	public String getScreenId(Class<?> c)
	{
		return screenIds.getOrDefault(c, "");
	}

	/**
	 * @deprecated This only exists for backwards compat purposes. Please use UIData.INSTANCE instead!
	 */
	@Deprecated
	public static UIData get()
	{
		return INSTANCE;
	}
}
