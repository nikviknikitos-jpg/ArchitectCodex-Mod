package com.example.architect.client;

import com.example.architect.entity.CorruptedColossusEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ColossusRenderer extends EntityRenderer<CorruptedColossusEntity> {
    private final BlockRenderManager brm;
    public ColossusRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.brm = ctx.getBlockRenderManager();
    }
    @Override public Identifier getTexture(CorruptedColossusEntity e) {
        return new Identifier("minecraft", "textures/block/obsidian.png");
    }
    @Override
    public void render(CorruptedColossusEntity e, float yaw, float tickDelta, MatrixStack m,
                       VertexConsumerProvider vcp, int light) {
        float age = e.age + tickDelta;
        m.push();
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
        for (int i = 0; i < 3; i++) {
            m.push();
            m.translate(0, i * 1.2, 0);
            brm.renderBlockAsEntity(Blocks.OBSIDIAN.getDefaultState(), m, vcp, light, OverlayTexture.DEFAULT_UV);
            m.pop();
        }
        m.push();
        m.translate(0, 4.0, 0);
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(age * 20));
        brm.renderBlockAsEntity(Blocks.MAGMA_BLOCK.getDefaultState(), m, vcp, light, OverlayTexture.DEFAULT_UV);
        m.pop();
        m.pop();
    }
}
