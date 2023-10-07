package top.offsetmonkey538.compactmobfarms.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import top.offsetmonkey538.compactmobfarms.screen.CompactMobFarmScreenHandler;

import static top.offsetmonkey538.compactmobfarms.CompactMobFarms.*;

public class CompactMobFarmScreen extends HandledScreen<CompactMobFarmScreenHandler> {
    private static final Quaternionf ENTITY_ROTATION = new Quaternionf().rotationZ((float) Math.PI);
    private static final Vector3f FRONT_UP_LIGHT = new Vector3f(0, -1, 0.5f).normalize();
    private static final Identifier TEXTURE = id("textures/gui/container/compact_mob_farm.png");
    private LivingEntity entity;
    private float maxEntityHealth = -1;
    private float currentEntityHealth = -1;
    private Text healthText = Text.empty();
    private int healthX;

    public CompactMobFarmScreen(CompactMobFarmScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        setEntity(handler.getEntityType());

        setEntityHealth(handler.getEntityHealth());
        setMaxEntityHealth(handler.getMaxEntityHealth());

        if (handler.getEntityHealth() == -1 || handler.getMaxEntityHealth() == -1) resetEntityHealth();

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
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        context.drawText(this.textRenderer, healthText, healthX, 70, 0xF82423, false);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);


        if (entity == null) return;

        int entityX = x + 140;
        int entityY = y + 63;

        renderEntity(context, entity, entityX, entityY, 42, ENTITY_ROTATION);
    }

    public static void renderEntity(DrawContext context, LivingEntity entity, int x, int y, int boxSize, Quaternionf rotation) {
        float entityScale = boxSize / Math.max(entity.getWidth(), entity.getHeight());

        context.getMatrices().push();
        context.getMatrices().translate(x, y, 50);
        context.getMatrices().multiply(rotation);
        context.getMatrices().scale(entityScale, entityScale, entityScale);
        RenderSystem.setShaderLights(FRONT_UP_LIGHT, FRONT_UP_LIGHT);

        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadows(false);
        entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 0, context.getMatrices(), context.getVertexConsumers(), 0xF000F0);
        entityRenderDispatcher.setRenderShadows(true);

        context.draw();
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
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

    public void resetEntityHealth() {
        this.maxEntityHealth = -1;
        this.currentEntityHealth = -1;

        healthText = Text.empty();
    }

    public void setEntityHealth(float newHealth) {
        this.currentEntityHealth = newHealth;

        healthText = Text.of(newHealth + " / " + maxEntityHealth);
        healthX = 115 + (50 - textRenderer.getWidth(healthText)) / 2;
    }

    public void setMaxEntityHealth(float newMaxHealth) {
        this.maxEntityHealth = newMaxHealth;

        healthText = Text.of(currentEntityHealth + " / " + newMaxHealth);
        healthX = 115 + (50 - textRenderer.getWidth(healthText)) / 2;
    }
}
