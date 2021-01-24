package dev.latvian.kubejs.ui;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResultHolder;

@Environment(EnvType.CLIENT)
public interface SetScreenEvent
{
	Event<SetScreenEvent> EVENT = EventFactory.createInteractionResultHolder(SetScreenEvent.class);

	InteractionResultHolder<Screen> modifyScreen(Screen screen);
}
