package dev.latvian.kubejs.ui;

import me.shedaniel.architectury.event.Actor;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class DummyGuiOpenEvent
{
	public static final Event<Actor<DummyGuiOpenEvent>> EVENT = EventFactory.createActorLoop(DummyGuiOpenEvent.class);

	private @Nullable Screen gui;

	public DummyGuiOpenEvent(@Nullable Screen gui)
	{
		this.setGui(gui);
	}

	public @Nullable Screen getGui()
	{
		return this.gui;
	}

	public void setGui(@Nullable Screen gui)
	{
		this.gui = gui;
	}
}
