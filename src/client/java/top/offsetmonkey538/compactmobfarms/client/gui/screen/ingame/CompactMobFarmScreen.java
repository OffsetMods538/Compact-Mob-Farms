package top.offsetmonkey538.compactmobfarms.client.gui.screen.ingame;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.compactmobfarms.screen.CompactMobFarmScreenHandler;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class CompactMobFarmScreen extends HandledScreen<CompactMobFarmScreenHandler> {
    private static final Identifier TEXTURE = id("textures/gui/container/compact_mob_farm.png");
    private LivingEntity entity;

    public CompactMobFarmScreen(CompactMobFarmScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        setEntity(handler.getEntityType());

        // TODO: use translations for this
        addDrawableChild(
                CyclingButtonWidget
                        .onOffBuilder(Text.of("On"), Text.of("Off"))
                        .initially(handler.isTurnedOn())
                        .omitKeyText()
                        .build(x + 10,  y + 14, 20, 20, null, (button, value) -> {
                            if (client == null || client.interactionManager == null) return;
                            this.client.interactionManager.clickButton(handler.syncId, value ? 1 : 0);
                        })
        );

        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        //RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        //RenderSystem.setShaderColor(1, 1, 1, 1);
        //RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);


        if (entity == null) return;

        int entityX = x + 118;
        int entityY = y + 20;
        // TODO: I don't like that it's following the cursor.
        //  Create a new method and make it render the entity like uhh isometrically or something similar
        //  Also it's not in the correct place because why would it be ._.
        InventoryScreen.drawEntity(context, entityX, entityY, 46, entityX - mouseX, entityY - mouseY, entity);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        super.render(context, mouseX, mouseY, delta);

        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    public void setEntity(EntityType<?> entityType) {
        if (entityType == null) {
            this.entity = null;
            return;
        }

        final Entity entity = entityType.create(client.world);
        if (!(entity instanceof LivingEntity livingEntity)) {
            this.entity = null;
            return;
        }

        this.entity = livingEntity;
    }

    public void clearEntity() {
        this.entity = null;
    }
}
