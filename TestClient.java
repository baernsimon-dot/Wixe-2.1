package com.example.testclient;

import com.example.testclient.gui.ClickGui;
import com.example.testclient.modules.Module;
import com.example.testclient.modules.ModuleManager;
import com.example.testclient.util.HudStack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("testclient");
    public static KeyBinding openGui;

    @Override
    public void onInitializeClient() {
        ModuleManager.init();

        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.testclient.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            ModuleManager.tick();
            while (openGui.wasPressed()) mc.setScreen(new ClickGui());
        });

        HudElementRegistry.addLast(Identifier.of("testclient", "hud"), (ctx, tick) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.options.hudHidden || mc.player == null) return;
            HudStack.reset();
            for (Module m : ModuleManager.MODULES) {
                if (!m.isEnabled() || m.pending) continue;
                try { m.onHud(ctx); }
                catch (Throwable t) { LOGGER.error("HUD-Fehler in {}", m.name, t); m.setEnabled(false); }
            }
        });
    }
}
