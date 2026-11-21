package com.example.architect.client;

import com.example.architect.entity.PlasmaBoltEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class PlasmaBoltRenderer extends EntityRenderer<PlasmaBoltEntity> {
    private final BlockRenderManager brm;
    public PlasmaBoltRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.brm = ctx.getBlockRenderManager();
    }
    @Override public Identifier getTexture(PlasmaBoltEntity e) {
        return new Identifier("minecraft", "textures/block/sea_lantern.png");
    }
    @Override
    public void render(PlasmaBoltEntity e, float yaw, float tickDelta, MatrixStack m,
                       VertexConsumerProvider vcp, int light) {
        m.push();
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((e.age + tickDelta) * 30));
        float s = 0.3f;
        m.scale(s, s, s);
        brm.renderBlockAsEntity(Blocks.SEA_LANTERN.getDefaultState(), m, vcp, light, OverlayTexture.DEFAULT_UV);
        m.pop();
    }
}
