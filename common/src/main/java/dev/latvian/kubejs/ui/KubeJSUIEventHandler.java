package dev.latvian.kubejs.ui;

import dev.latvian.kubejs.script.ScriptType;
import net.minecraft.client.gui.screens.Screen;

/**
 * @author LatvianModder
 */
public class KubeJSUIEventHandler
{
	public static Screen openGui(Screen screen)
	{
		Screen original = screen;

		if (original == null)
		{
			return screen;
		}
		else if (original instanceof ScreenKubeJSUI)
		{
			ScreenKubeJSUI o = (ScreenKubeJSUI) original;

			try
			{
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + o.screenId) && e.consumer != null)
				{
					screen = new ScreenKubeJSUI(o.screenId, o.original, e.consumer, e.forcedScale);
				}
			}
			catch (Exception ex)
			{
				System.out.println("Failed to create " + o.screenId + " UI:");
				ex.printStackTrace();
			}

			return screen;
		}

		String id = UIData.get().getScreenId(original.getClass());

		if (!id.isEmpty())
		{
			try
			{
				UIEventJS e = new UIEventJS();

				if (e.post(ScriptType.CLIENT, "ui." + id) && e.consumer != null)
				{
					screen = new ScreenKubeJSUI(id, original, e.consumer, e.forcedScale);
				}
			}
			catch (Exception ex)
			{
				System.out.println("Failed to create " + id + " UI:");
				ex.printStackTrace();
			}
		}

		return screen;
	}
}