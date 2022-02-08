package dev.latvian.kubejs.ui;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class KubeJSUIClient {
	static ThreadLocal<Boolean> withinInitPreHacks = ThreadLocal.withInitial(() -> false);
	private static ShaderInstance backgroundShaderInstance;
	private static ShaderInstance positionColorTexShaderInstance;
	public static Uniform RESOLUTION_UNIFORM;
	public static Uniform TIME_UNIFORM;
	public static Uniform TICK_UNIFORM;
	public static Uniform MOUSE_UNIFORM;
	public static Uniform GUI_SCALE_UNIFORM;

	public static ShaderInstance getBackgroundShader() {
		return backgroundShaderInstance;
	}

	public static ShaderInstance getPositionColorTexShader() {
		return positionColorTexShaderInstance;
	}

	public static void init() {
		ClientGuiEvent.SET_SCREEN.register(KubeJSUIEventHandler::openGui);

		if (!Platform.isForge()) {
			ClientGuiEvent.INIT_PRE.register((screen, access) -> {
				if (withinInitPreHacks.get()) {
					return EventResult.pass();
				}
				if (Minecraft.getInstance().screen != screen) {
					return EventResult.pass();
				}
				if (screen instanceof ScreenKubeJSUI) {
					return EventResult.pass();
				}

				String screenId = UIData.INSTANCE.getScreenId(screen.getClass());

				if (!screenId.isEmpty()) {
					// Re-open the screen
					withinInitPreHacks.set(true);
					try {
						Minecraft.getInstance().setScreen(screen);
					} finally {
						withinInitPreHacks.set(false);
					}
					return EventResult.interruptTrue();
				}

				return EventResult.pass();
			});
		}

		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, UIData.INSTANCE);
		ClientReloadShadersEvent.EVENT.register(KubeJSUIClient::reloadShaders);
	}

	private static void reloadShaders(ResourceManager manager, ClientReloadShadersEvent.ShadersSink sink) {
		try {
			sink.registerShader(new ShaderInstance(manager, "kubejsui_background", DefaultVertexFormat.POSITION), i -> {
				backgroundShaderInstance = i;
				RESOLUTION_UNIFORM = i.getUniform("resolution");
				TIME_UNIFORM = i.getUniform("time");
				TICK_UNIFORM = i.getUniform("tick");
				MOUSE_UNIFORM = i.getUniform("mouse");
				GUI_SCALE_UNIFORM = i.getUniform("guiScale");
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			sink.registerShader(new ShaderInstance(manager, "kubejsui_position_color_tex", DefaultVertexFormat.POSITION_COLOR_TEX), i -> {
				positionColorTexShaderInstance = i;
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
