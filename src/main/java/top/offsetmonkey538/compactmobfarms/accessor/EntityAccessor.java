package top.offsetmonkey538.compactmobfarms.accessor;

import java.util.function.Consumer;
import net.minecraft.item.ItemStack;

public interface EntityAccessor {

    void compact_mob_farms$setDropMethod(Consumer<ItemStack> dropMethod);
}
