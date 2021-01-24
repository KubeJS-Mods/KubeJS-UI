package dev.latvian.kubejs.ui.mixin.fabric;

import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EffectInstance.class)
public class EffectInstanceMixin
{
	@Redirect(
			method = "<init>",
			at = @At(value = "NEW",
					target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
					ordinal = 0)
	)
	private ResourceLocation mojangPls(String _0, ResourceManager rm, String str)
	{
		return kjs$mojangPls(new ResourceLocation(str), ".json");
	}

	@Redirect(
			method = "getOrCreate",
			at = @At(value = "NEW",
					target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
					ordinal = 0)
	)
	private static ResourceLocation mojangPls(String _0, ResourceManager rm, Program.Type type, String str)
	{
		return kjs$mojangPls(new ResourceLocation(str), type.getExtension());
	}

	private static ResourceLocation kjs$mojangPls(ResourceLocation rl, String ext)
	{
		return new ResourceLocation(rl.getNamespace(), "shaders/program/" + rl.getPath() + ext);
	}
}
