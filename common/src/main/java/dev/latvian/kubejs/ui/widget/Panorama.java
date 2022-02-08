package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @author LatvianModder
 */
public class Panorama extends Widget {
	private final CubeMap cubeMap;
	public static float panoramaTime;
	public float speed = 1F;

	public Panorama(ResourceLocation cm) {
		cubeMap = new CubeMap(cm);
		setW(64);
		setH(64);
	}

	@Override
	public void renderBackground(PoseStack matrixStack, float partialTicks) {
		panoramaTime += partialTicks * speed;
		cubeMap.render(Minecraft.getInstance(), Mth.sin(panoramaTime * 0.001F) * 5F + 25F, -panoramaTime * 0.1F, 1F);
	}
}