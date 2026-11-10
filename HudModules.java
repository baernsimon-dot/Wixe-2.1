package com.example.testclient.modules;

import com.example.testclient.util.Gfx;
import com.example.testclient.util.HudStack;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

final class CoordsHud extends Module {
    CoordsHud() { super("Coords", "Render", "Zeigt Koordinaten und FPS"); }

    @Override public void onHud(DrawContext ctx) {
        if (mc.player == null) return;
        List<String> l = new ArrayList<>();
        l.add(String.format(Locale.ROOT, "XYZ %.1f %.1f %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ()));
        l.add("FPS " + mc.getCurrentFps());
        HudStack.panel(ctx, "Info", l);
    }
}

final class ModuleList extends Module {
    ModuleList() { super("ModuleList", "Render", "Zeigt aktive Module rechts oben"); }

    @Override public void onHud(DrawContext ctx) {
        List<Module> on = new ArrayList<>();
        for (Module m : ModuleManager.MODULES) if (m.isEnabled() && m != this) on.add(m);
        on.sort(Comparator.comparingInt((Module m) -> -Gfx.width(m.name)));
        int sw = mc.getWindow().getScaledWidth(), y = 6;
        for (Module m : on) {
            int w = Gfx.width(m.name) + 12;
            Gfx.round(ctx, sw - w - 6, y, w, 14, 6, 0xB0101018);
            Gfx.text(ctx, m.name, sw - w - 6 + 6, y + 3, 0xFF9B7BFF);
            y += 16;
        }
    }
}
