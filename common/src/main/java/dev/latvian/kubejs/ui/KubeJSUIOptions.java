package dev.latvian.kubejs.ui;

import dev.architectury.platform.Platform;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class KubeJSUIOptions {
	private static KubeJSUIOptions instance;
	public boolean useShaders;
	public int defaultShaderScale;

	public static KubeJSUIOptions getInstance() {
		if (instance == null) {
			instance = new KubeJSUIOptions();
			Map<String, String> map = new HashMap<>();
			boolean loaded = false;

			try {
				Path file = Platform.getGameFolder().resolve("optionskubejsui.txt");

				if (Files.exists(file)) {
					for (String s : Files.readAllLines(file)) {
						String[] s1 = s.trim().split(":", 2);

						if (s1.length == 2) {
							map.put(s1[0].trim(), s1[1].trim());
						}
					}

					loaded = true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			instance.useShaders = map.getOrDefault("useShaders", "true").equals("true");
			instance.defaultShaderScale = Math.max(1, Integer.parseInt(map.getOrDefault("defaultShaderScale", "1")));

			if (!loaded) {
				instance.save();
			}
		}

		return instance;
	}

	public void save() {
		Path file = Platform.getGameFolder().resolve("optionskubejsui.txt");

		try (PrintWriter printwriter = new PrintWriter(Files.newBufferedWriter(file))) {
			printwriter.println("useShaders: " + useShaders);
			printwriter.println("defaultShaderScale: " + defaultShaderScale);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
