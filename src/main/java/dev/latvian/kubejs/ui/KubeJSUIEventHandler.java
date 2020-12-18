package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.script.ScriptType;
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
			ScreenKubeJSUI o = (ScreenKubeJSUI) original;

			try
			{
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + o.screenId) && e.consumer != null)
				{
					event.setGui(new ScreenKubeJSUI(o.screenId, o.original, e.consumer, e.forcedScale));
				}
			}
			catch (Exception ex)
			{
				System.out.println("Failed to create " + o.screenId + " UI:");
				ex.printStackTrace();
			}

			return;
		}

		String id = UIData.get().getScreenId(original.getClass());

		if (!id.isEmpty())
		{
			try
			{
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + id) && e.consumer != null)
				{
					event.setGui(new ScreenKubeJSUI(id, original, e.consumer, e.forcedScale));
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
