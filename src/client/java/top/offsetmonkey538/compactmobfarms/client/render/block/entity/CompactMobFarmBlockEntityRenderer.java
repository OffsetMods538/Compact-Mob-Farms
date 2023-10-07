package top.offsetmonkey538.compactmobfarms.client.render.block.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import top.offsetmonkey538.compactmobfarms.block.entity.CompactMobFarmBlockEntity;
import top.offsetmonkey538.compactmobfarms.item.FilledSampleTakerItem;

public class CompactMobFarmBlockEntityRenderer implements BlockEntityRenderer<CompactMobFarmBlockEntity> {
    public CompactMobFarmBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(CompactMobFarmBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final ItemStack sampleTaker = blockEntity.getSampleTaker();
        if (sampleTaker == null) return;

        final EntityType<?> entityType = FilledSampleTakerItem.getSampledEntityType(sampleTaker);
        if (entityType == null) return;

        final Entity entity = entityType.create(blockEntity.getWorld());
        if (!(entity instanceof LivingEntity)) return;

        final Box boundingBox = entity.getBoundingBox();

        final float entityWidth = (float) (boundingBox.maxX - boundingBox.minX);
        final float entityHeight = (float) (boundingBox.maxY - boundingBox.minY);
        final float entityDepth = (float) (boundingBox.maxZ - boundingBox.minZ);

        final float size = Math.max(
                entityWidth,
                Math.max(
                        entityHeight,
                        entityDepth
                )
        );

        final float scale = 0.45f / size;

        matrices.push();

        matrices.translate(0.5f, 0.31f, 0.5f);
        matrices.scale(scale, scale, scale);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING).asRotation()));

        MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);

        matrices.pop();
    }
}
