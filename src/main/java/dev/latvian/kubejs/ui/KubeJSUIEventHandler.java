package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.script.ScriptType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = KubeJSUI.MOD_ID, value = Dist.CLIENT)
public class KubeJSUIEventHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void openGui(GuiOpenEvent event)
	{
		Screen original = event.getGui();

		if (original == null)
		{
			return;
		}
		else if (original instanceof ScreenKubeJSUI)
		{
			original = ((ScreenKubeJSUI) original).original;
		}

		String id = UIData.get().getScreenId(original.getClass());

		if (!id.isEmpty())
		{
			try
			{
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + id) && e.consumer != null)
				{
					Minecraft mc = Minecraft.getInstance();
					int prevScale = mc.options.guiScale;

					if (e.forcedScale >= 0)
					{
						mc.options.guiScale = e.forcedScale;
						mc.resizeDisplay();
					}

					event.setGui(new ScreenKubeJSUI(id, original, e.consumer, prevScale));
				}
			}
			catch (Exception ex)
			{
				System.out.println("Failed to create " + id + " UI:");
				ex.printStackTrace();
			}
		}
	}
}
