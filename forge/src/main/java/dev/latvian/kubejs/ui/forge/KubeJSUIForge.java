package dev.latvian.kubejs.ui.forge;

import dev.latvian.kubejs.ui.KubeJSUI;
import dev.latvian.kubejs.ui.SetScreenEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;

@Mod(KubeJSUI.MOD_ID)
public class KubeJSUIForge
{
	public KubeJSUIForge()
	{
		KubeJSUI.init();
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::modifyScreen);
	}

	public void modifyScreen(GuiOpenEvent event)
	{
		InteractionResultHolder<Screen> result = SetScreenEvent.EVENT.invoker().modifyScreen(event.getGui());
		if (result.getResult() == InteractionResult.FAIL)
		{
			event.setCanceled(true);
			return;
		}
		event.setGui(result.getObject());
	}
}
