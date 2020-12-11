package dev.latvian.kubejs.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.ui.widget.UI;
import dev.latvian.kubejs.ui.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.shader.ShaderInstance;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public final class ScreenKubeJSUI extends Screen
{
	public final String screenId;
	public final Screen original;
	public final Consumer<UI> consumer;
	public final UI ui;
	public final Map<ResourceLocation, Optional<ShaderInstance>> shaders;

	public ScreenKubeJSUI(String i, Screen o, Consumer<UI> c)
	{
		super(o.getTitle());
		screenId = i;
		original = o;
		consumer = c;
		ui = new UI(this);
		shaders = new HashMap<>();
	}

	@Override
	public void init(Minecraft mc, int w, int h)
	{
		original.init(mc, w, h);
		super.init(mc, w, h);
	}

	@Override
	public void init()
	{
		ui.children.clear();
		ui.allWidgets.clear();
		consumer.accept(ui);
		ui.collectWidgets(ui.allWidgets);

		for (Widget w : ui.allWidgets)
		{
			w.actualX = w.getX();
			w.actualY = w.getY();
		}
	}

	public FontRenderer getUiFont()
	{
		return font;
	}

	@Override
	public boolean shouldCloseOnEsc()
	{
		return original.shouldCloseOnEsc();
	}

	@Override
	public boolean isPauseScreen()
	{
		return original.isPauseScreen();
	}

	public void clearCaches()
	{
		for (Optional<ShaderInstance> instance : shaders.values())
		{
			try
			{
				instance.ifPresent(ShaderInstance::close);
			}
			catch (Exception ex)
			{
			}
		}

		shaders.clear();
	}

	@Override
	public void removed()
	{
		clearCaches();
		super.removed();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		original.renderBackground(matrixStack);
		ui.mouse.x = mouseX;
		ui.mouse.y = mouseY;

		for (Widget w : ui.allWidgets)
		{
			boolean b = w.isMouseOver;
			w.isMouseOver = mouseX >= w.actualX && mouseY >= w.actualY && mouseX < w.actualX + w.getWidth() && mouseY < w.actualY + w.getHeight();

			if (b != w.isMouseOver)
			{
				if (w.isMouseOver)
				{
					if (w.mouseEnter != null)
					{
						w.mouseEnter.run();
					}
				}
				else if (w.mouseExit != null)
				{
					w.mouseExit.run();
				}
			}
		}

		ui.renderBackground(matrixStack, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		ui.renderForeground(matrixStack, partialTicks);
	}

	@Override
	public boolean mouseClicked(double x, double y, int button)
	{
		if (ui.mousePressed())
		{
			return true;
		}

		return super.mouseClicked(x, y, button);
	}

	@Override
	public boolean mouseReleased(double x, double y, int button)
	{
		ui.mouseReleased();
		return super.mouseReleased(x, y, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (keyCode == GLFW.GLFW_KEY_F5)
		{
			ui.tick = 0;

			if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0)
			{
				KubeJS.clientScriptManager.unload();
				KubeJS.clientScriptManager.loadFromDirectory();
				KubeJS.clientScriptManager.load();
				minecraft.setScreen(this);
			}

			return true;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	public Optional<ShaderInstance> loadShader(ResourceLocation id)
	{
		Optional<ShaderInstance> instance = shaders.get(id);

		if (instance != null)
		{
			return instance;
		}

		try
		{
			instance = Optional.of(new ShaderInstance(Minecraft.getInstance().getResourceManager(), id.toString()));
		}
		catch (Exception ex)
		{
			instance = Optional.empty();
			KubeJS.LOGGER.error("Failed to load shader " + id + ":");
			ex.printStackTrace();
		}

		shaders.put(id, instance);
		return instance;
	}

	@Override
	public void tick()
	{
		super.tick();
		ui.tick++;
	}
}