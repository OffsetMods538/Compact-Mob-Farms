package top.offsetmonkey538.compactmobfarms.network;

import net.minecraft.util.Identifier;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public final class ModPackets {
    private ModPackets() {

    }

    public static Identifier GUI_ENTITY_CHANGED = id("gui_entity_changed");
    public static Identifier GUI_ENTITY_REMOVED = id("gui_entity_removed");
}
