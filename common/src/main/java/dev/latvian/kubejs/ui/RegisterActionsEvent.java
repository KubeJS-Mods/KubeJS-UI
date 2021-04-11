package dev.latvian.kubejs.ui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface RegisterActionsEvent {
	void registerActions(BiConsumer<ResourceLocation, Consumer<Screen>> registry);
}
