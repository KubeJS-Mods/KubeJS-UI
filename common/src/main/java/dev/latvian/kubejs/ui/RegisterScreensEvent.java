package dev.latvian.kubejs.ui;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface RegisterScreensEvent {
	void registerScreens(BiConsumer<Class<?>, String> registry);
}
