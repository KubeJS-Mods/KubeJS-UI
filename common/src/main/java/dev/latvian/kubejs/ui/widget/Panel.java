package dev.latvian.kubejs.ui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.kubejs.ui.KubeJSUIOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class Panel extends Widget {
	public final List<Widget> children = new ArrayList<>();

	@Override
	public void collectWidgets(List<Widget> list) {
		super.collectWidgets(list);

		for (Widget w : children) {
			w.collectWidgets(list);
		}
	}

	public <W extends Widget> void widget(Supplier<W> s, Consumer<W> c) {
		W w = s.get();
		w.parent = this;
		children.add(w);
		c.accept(w);
	}

	public void panel(Consumer<Panel> c) {
		widget(Panel::new, c);
	}

	public void button(Consumer<Button> c) {
		widget(Button::new, c);
	}

	public void imageButton(Consumer<ImageButton> c) {
		widget(ImageButton::new, c);
	}

	public void label(Consumer<Label> c) {
		widget(Label::new, c);
	}

	public void image(Consumer<Image> c) {
		widget(Image::new, c);
	}

	public void background(String texture) {
		image(image -> {
			image.setTexture(texture);
			image.setX(0);
			image.setY(0);
			image.setWidth(getWidth());
			image.setHeight(getHeight());
		});
	}

	public void tilingBackground(String texture, int size) {
		image(image -> {
			image.setTexture(texture);
			image.setTileSize(size);
			image.setX(0);
			image.setY(0);
			image.setWidth(getWidth());
			image.setHeight(getHeight());
		});
	}

	public void fillBackground(String texture, int tw, int th) {
		image(image -> {
			image.setTexture(texture);

			double fw = getWidth() / (double) tw;
			double fh = getHeight() / (double) th;

			if (fw > fh) {
				image.setWidth((int) (tw * fw));
				image.setHeight((int) (th * fw));
			} else {
				image.setWidth((int) (tw * fh));
				image.setHeight((int) (th * fh));
			}

			image.setX((getWidth() - image.getWidth()) / 2);
			image.setY((getHeight() - image.getHeight()) / 2);
		});
	}

	public void shaderBackground(String s, int scale) {
		if (KubeJSUIOptions.getInstance().useShaders) {
			widget(() -> new Shader(getUi().screen.loadShader(new ResourceLocation(s)).orElse(null), scale), shader -> {
				shader.setX(0);
				shader.setY(0);
				shader.setWidth(getWidth());
				shader.setHeight(getHeight());
			});
		}
	}

	public void shaderBackground(String s) {
		shaderBackground(s, KubeJSUIOptions.getInstance().defaultShaderScale);
	}

	public void minecraftLogo(int x, int y) {
		image(i -> {
			i.setTexture("minecraft:textures/gui/title/minecraft.png");
			i.setX(x);
			i.setY(y);
			i.setWidth(155);
			i.setHeight(44);
			i.setUv(new int[]{0, 0, 155, 44});
		});

		image(i -> {
			i.setTexture("minecraft:textures/gui/title/minecraft.png");
			i.setX(x + 155);
			i.setY(y);
			i.setWidth(155);
			i.setHeight(44);
			i.setUv(new int[]{0, 45, 155, 44});
		});
	}

	public void minecraftLogo(int y) {
		minecraftLogo(getWidth() / 2 - 137, y);
	}

	public void panorama(ResourceLocation cubeMap) {
		widget(() -> new Panorama(cubeMap), shader -> {
			shader.setX(0);
			shader.setY(0);
			shader.setWidth(getWidth());
			shader.setHeight(getHeight());
		});
	}

	public void minecraftPanorama() {
		panorama(new ResourceLocation("textures/gui/title/background/panorama"));
	}

	@Override
	public void renderBackground(PoseStack matrixStack, float partialTicks) {
		for (Widget w : children) {
			w.renderBackground(matrixStack, partialTicks);
		}
	}

	@Override
	public void renderForeground(PoseStack matrixStack, float partialTicks) {
		for (Widget w : children) {
			w.renderForeground(matrixStack, partialTicks);
		}
	}

	@Override
	public boolean mousePressed() {
		for (Widget w : children) {
			if (w.mousePressed()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void mouseReleased() {
		for (Widget w : children) {
			w.mouseReleased();
		}
	}

	@Override
	public void appendHoverText(List<Component> list) {
		super.appendHoverText(list);

		for (Widget w : children) {
			w.appendHoverText(list);
		}
	}
}
