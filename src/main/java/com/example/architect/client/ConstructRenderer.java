package com.example.architect.client;

import com.example.architect.entity.ConstructEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ConstructRenderer extends EntityRenderer<ConstructEntity> {
    private final BlockRenderManager brm;
    public ConstructRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.brm = ctx.getBlockRenderManager();
    }

    @Override public Identifier getTexture(ConstructEntity e) {
        return new Identifier("minecraft", "textures/block/copper_block.png");
    }

    @Override
    public void render(ConstructEntity e, float yaw, float tickDelta, MatrixStack m,
                       VertexConsumerProvider vcp, int light) {
        float age = e.age + tickDelta;
        m.push();
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
        m.scale(1f, 1f, 1f);
        // body
        m.push();
        brm.renderBlockAsEntity(Blocks.COPPER_BLOCK.getDefaultState(), m, vcp, light, OverlayTexture.DEFAULT_UV);
        m.pop();
        // head
        m.push();
        m.translate(0, 1.1, 0);
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(age * 15));
        brm.renderBlockAsEntity(Blocks.CARVED_PUMPKIN.getDefaultState(), m, vcp, light, OverlayTexture.DEFAULT_UV);
        m.pop();
        // rotating tool
        m.push();
        m.translate(0.7, 0.6, 0.7);
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(age * 240));
        float s = 0.5f;
        m.scale(s, s, s);
        brm.renderBlockAsEntity(Blocks.DIAMOND_BLOCK.getDefaultState(), m, vcp, light, OverlayTexture.DEFAULT_UV);
        m.pop();
        m.pop();
    }
}
