package dev.latvian.kubejs.ui.mixin.fabric;


import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import dev.latvian.kubejs.ui.SetScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin
{
	@Shadow
	public abstract void setScreen(@Nullable Screen screen);

	// is there really no better way to do this?
	private boolean kjs$setScreenCancelled = false;

	private String kjs$hostname;
	private int kjs$port;

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

	@Redirect(
			method = "<init>",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"),
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;resizeDisplay()V"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;registerTextures(Lnet/minecraft/client/Minecraft;)V")
			)
	)
	public void minecraftWhy(Minecraft mc, Screen screen)
	{
	}

	@Inject(
			method = "<init>",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;resizeDisplay()V"),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void iHateThis(GameConfig gc, CallbackInfo ci, File f, String string2, int j)
	{
		kjs$hostname = string2;
		kjs$port = j;
	}

	@Inject(
			// <init>.lambda$null$1
			method = {"method_29338", "lambda$null$1"},
			at = @At("RETURN"),
			locals = LocalCapture.CAPTURE_FAILSOFT
	)
	public void registerMainScreens(CallbackInfo ci)
	{
		if (kjs$hostname != null)
		{
			setScreen(new ConnectScreen(new TitleScreen(), (Minecraft) ((Object) this), kjs$hostname, kjs$port));
		}
		else
		{
			setScreen(new TitleScreen(true));
		}
	}
}
