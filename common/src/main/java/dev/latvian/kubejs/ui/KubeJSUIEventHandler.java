package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.script.ScriptType;
import net.minecraft.client.gui.screens.Screen;

/**
 * @author LatvianModder
 */
public class KubeJSUIEventHandler
{
	public static void openGui(DummyGuiOpenEvent event)
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