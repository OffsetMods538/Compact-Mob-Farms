package top.offsetmonkey538.compactmobfarms.network;

import net.minecraft.util.Identifier;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public final class ModPackets {
    private ModPackets() {

    }

    public static final Identifier GUI_ENTITY_CHANGED       = id("gui_entity_changed");
    public static final Identifier GUI_ENTITY_REMOVED       = id("gui_entity_removed");

    public static final Identifier GUI_HEALTH_RESET         = id("gui_health_reset");
    public static final Identifier GUI_HEALTH_CHANGED       = id("gui_health_changed");
    public static final Identifier GUI_MAX_HEALTH_CHANGED   = id("gui_max_health_changed");

    public static final Identifier GUI_UPDATE_ATTACK_SPEED  = id("gui_update_attack_speed");
    public static final Identifier GUI_UPDATE_ATTACK_DAMAGE = id("gui_update_attack_damage");
}
