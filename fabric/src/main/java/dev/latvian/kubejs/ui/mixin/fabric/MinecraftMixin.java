package dev.latvian.kubejs.ui.mixin.fabric;


import dev.latvian.kubejs.ui.SetScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
	// is there really no better way to do this?
	private boolean kjs$setScreenCancelled = false;

	@ModifyVariable(
			method = "setScreen",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/player/LocalPlayer;respawn()V",
					shift = At.Shift.BY,
					by = 2),
			argsOnly = true
	)
	public Screen modifyScreen(Screen screen)
	{
		Screen old = screen;
		InteractionResultHolder<Screen> event = SetScreenEvent.EVENT.invoker().modifyScreen(screen);
		if (event.getResult() == InteractionResult.FAIL)
		{
			kjs$setScreenCancelled = true;
			return old;
		}
		kjs$setScreenCancelled = false;
		screen = event.getObject();
		if (old != null && screen != old)
		{
			old.removed();
		}
		return screen;
	}

	@Inject(
			method = "setScreen",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/player/LocalPlayer;respawn()V",
					shift = At.Shift.BY,
					by = 3),
			cancellable = true
	)
	public void cancelSetScreen(@Nullable Screen screen, CallbackInfo ci)
	{
		if (kjs$setScreenCancelled)
		{
			ci.cancel();
		}
	}
}
