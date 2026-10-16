package com.example.ribbon.render;

import com.example.ribbon.Config;
import com.example.ribbon.ShadowRendererDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = "ribbon")
public final class BlockEntityCullingTickHandler {

    /** 扫描半径（区块）。8 = 玩家周围 17×17 区块 */
    private static final int CHUNK_RADIUS = 8;

    private static int tickCounter = 0;

    private BlockEntityCullingTickHandler() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!Config.isBlockEntityCullingEnabled()) return;
        if (ShadowRendererDetector.isShadowPassActive()) return;

        tickCounter++;
        if (tickCounter < 3) return;
        tickCounter = 0;

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null || mc.player == null) return;

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

        int centerX = mc.player.chunkPosition().x;
        int centerZ = mc.player.chunkPosition().z;

        List<BlockEntity> list = new ArrayList<>();
        for (int dx = -CHUNK_RADIUS; dx <= CHUNK_RADIUS; dx++) {
            for (int dz = -CHUNK_RADIUS; dz <= CHUNK_RADIUS; dz++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(centerX + dx, centerZ + dz);
                if (chunk != null) {
                    list.addAll(chunk.getBlockEntities().values());
                }
            }
        }

        BlockEntityCullingManager.submitBatch(list, cameraPos, level);
    }
}