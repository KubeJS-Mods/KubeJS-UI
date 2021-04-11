package dev.latvian.kubejs.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.util.UtilsJS;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.realms.RealmsBridge;
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
public enum UIData implements ResourceManagerReloadListener {
	INSTANCE;

	Event<RegisterScreensEvent> REGISTER_SCREENS = EventFactory.createLoop();
	Event<RegisterActionsEvent> REGISTER_ACTIONS = EventFactory.createLoop();

	private final Map<Class<?>, String> screenIds = new HashMap<>();
	private final Map<ResourceLocation, Consumer<Screen>> actions = new HashMap<>();

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		screenIds.clear();
		actions.clear();

		screenIds.put(TitleScreen.class, "main_menu");
		screenIds.put(RealmsBridge.class, "realms");
		screenIds.put(LanguageSelectScreen.class, "language");
		screenIds.put(OptionsScreen.class, "options");
		screenIds.put(AccessibilityOptionsScreen.class, "accessibility");
		screenIds.put(SelectWorldScreen.class, "select_world");
		screenIds.put(WinScreen.class, "credits");

		actions.put(new ResourceLocation("minecraft:singleplayer"), VanillaActions.SINGLEPLAYER);
		actions.put(new ResourceLocation("minecraft:multiplayer"), VanillaActions.MULTIPLAYER);
		actions.put(new ResourceLocation("minecraft:realms"), VanillaActions.REALMS);
		actions.put(new ResourceLocation("minecraft:language"), VanillaActions.LANGUAGE);
		actions.put(new ResourceLocation("minecraft:options"), VanillaActions.OPTIONS);
		actions.put(new ResourceLocation("minecraft:quit"), VanillaActions.QUIT);
		actions.put(new ResourceLocation("minecraft:accessibility"), VanillaActions.ACCESSIBILITY);

		actions.put(new ResourceLocation("kubejsui:toggle_shaders"), VanillaActions.KUBEJSUI_TOGGLE_SHADERS);

		REGISTER_SCREENS.invoker().registerScreens(screenIds::put);
		REGISTER_ACTIONS.invoker().registerActions(actions::put);

		registerPlatformScreensAndActions(screenIds, actions);

		Gson gson = new GsonBuilder().create();

		for (String namespace : resourceManager.getNamespaces()) {
			try {
				for (Resource resource : resourceManager.getResources(new ResourceLocation(namespace, "kubejsui.json"))) {
					try (InputStream stream = resource.getInputStream()) {
						JsonObject json = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);

						if (json.has("actions")) {
							for (Map.Entry<String, JsonElement> entry : json.get("actions").getAsJsonObject().entrySet()) {
								String s = entry.getValue().getAsString();

								try {
									int i = s.lastIndexOf('.');
									Class<?> clazz = Class.forName(s.substring(0, i));
									Field field = clazz.getDeclaredField(s.substring(i + 1));
									field.setAccessible(true);
									Consumer<Screen> consumer = Objects.requireNonNull((Consumer<Screen>) field.get(null));
									actions.put(new ResourceLocation(namespace, entry.getKey()), consumer);
								} catch (Throwable ex) {
									KubeJS.LOGGER.error("UI: Failed to load action " + entry.getKey() + ":" + entry.getValue() + ": " + ex);
								}
							}
						}

						if (json.has("screens")) {
							for (Map.Entry<String, JsonElement> entry : json.get("screens").getAsJsonObject().entrySet()) {
								if (Platform.isFabric()) {
									addMappedScreen(screenIds, entry);
								}
								try {
									screenIds.put(Class.forName(entry.getKey()), entry.getValue().getAsString());
								} catch (Throwable ex) {
									// KubeJS.LOGGER.error("UI: Failed to load screen " + entry.getKey() + ":" + entry.getValue() + ": " + ex);
								}
							}
						}
					}
				}
			} catch (Exception ignored) {
			}
		}
	}

	@ExpectPlatform
	private static void addMappedScreen(Map<Class<?>, String> screenIds, Map.Entry<String, JsonElement> entry) {
		throw new AssertionError();
	}

	@ExpectPlatform
	private static void registerPlatformScreensAndActions(Map<Class<?>, String> screenIds, Map<ResourceLocation, Consumer<Screen>> actions) {
		throw new AssertionError();
	}

	@Nullable
	public Consumer<Screen> getAction(String id) {
		return actions.get(UtilsJS.getMCID(id));
	}

	public String getScreenId(Class<?> c) {
		return screenIds.getOrDefault(c, "");
	}

	/**
	 * @deprecated This only exists for backwards compat purposes. Please use UIData.INSTANCE instead!
	 */
	@Deprecated
	public static UIData get() {
		return INSTANCE;
	}
}
