package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.script.ScriptType;
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
		if (event.getGui() == null)
		{
			return;
		}

		String id = UIData.get().getScreenId(event.getGui().getClass());

		if (!id.isEmpty())
		{
			try
			{
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + id) && e.consumer != null)
				{
					event.setGui(new ScreenKubeJSUI(id, event.getGui(), e.consumer));
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
