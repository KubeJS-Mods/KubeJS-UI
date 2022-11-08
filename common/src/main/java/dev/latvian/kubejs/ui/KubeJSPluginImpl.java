package dev.latvian.kubejs.ui;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public class KubeJSPluginImpl extends KubeJSPlugin {
    public static final EventGroup GROUP = EventGroup.of("KubeUiEvents");
    public static final EventHandler UI_EVENT = GROUP.client("ui", () -> UIEventJS.class).extra(Extra.REQUIRES_STRING).cancelable();

    @Override
    public void registerEvents() {
        GROUP.register();
    }
}
