package dev.latvian.kubejs.ui;

import me.shedaniel.architectury.event.Actor;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;

public class DummyGuiOpenEvent
{
	public static final Event<Actor<DummyGuiOpenEvent>> EVENT = EventFactory.createActorLoop(DummyGuiOpenEvent.class);

	private Screen gui;

	public DummyGuiOpenEvent(Screen gui)
	{
		this.setGui(gui);
	}

	public Screen getGui()
	{
		return this.gui;
	}

	public void setGui(Screen gui)
	{
		this.gui = gui;
	}
}
