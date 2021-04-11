package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author LatvianModder
 */
public class Panorama extends Widget {
	public final PanoramaRenderer panoramaRenderer;

	public Panorama(ResourceLocation cm) {
		panoramaRenderer = new PanoramaRenderer(new CubeMap(cm));
		setWidth(64);
		setHeight(64);
	}

	@Override
	public void renderBackground(PoseStack matrixStack, float partialTicks) {
		panoramaRenderer.render(partialTicks, 1F);
	}
}