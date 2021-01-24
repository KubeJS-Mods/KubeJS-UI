package dev.latvian.kubejs.ui.mixin.fabric;


import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.ui.DummyGuiOpenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
	@ModifyVariable(
			method = "setScreen",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/player/LocalPlayer;respawn()V",
					shift = At.Shift.AFTER,
					by = 2),
			argsOnly = true
	)
	public Screen modifyScreen(Screen screen)
	{
		KubeJS.LOGGER.warn("thang");
		Screen old = screen;
		DummyGuiOpenEvent event = new DummyGuiOpenEvent(screen);
		boolean kjs$cancelled = false;
		if (DummyGuiOpenEvent.EVENT.invoker().act(event) != InteractionResult.PASS)
		{
			kjs$cancelled = true;
			return old;
		}
		screen = event.getGui();
		if (old != null && screen != old)
		{
			old.onClose();
		}
		return screen;
	}

	@Inject(
			method = "setScreen",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/player/LocalPlayer;respawn()V",
					shift = At.Shift.AFTER,
					by = 2),
			cancellable = true,
			locals = LocalCapture.PRINT
	)
	public void cancelSetScreen(@Nullable Screen screen, CallbackInfo ci)
	{
		KubeJS.LOGGER.warn("I do not want to be here.");
	}
}
