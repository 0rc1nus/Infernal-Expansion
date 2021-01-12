package com.nekomaster1000.infernalexp.world.gen.carvers;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.nekomaster1000.infernalexp.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.CanyonWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class GlowstoneRavineCarver extends CanyonWorldCarver {

    public GlowstoneRavineCarver(Codec<ProbabilityConfig> p_i231916_1_) {
        super(p_i231916_1_);
        this.carvableBlocks = ImmutableSet.of(Blocks.BLACKSTONE, Blocks.GLOWSTONE, ModBlocks.DULLSTONE.get(), ModBlocks.DIMSTONE.get(), ModBlocks.GLOWDUST_SAND.get(), ModBlocks.GLOWDUST_STONE.get(), ModBlocks.GLOWDUST.get(), ModBlocks.CRUMBLING_BLACKSTONE.get());
    }

    private final float[] heightToHorizontalStretchFactor = new float[256];

    @Override
    public boolean carveRegion(IChunk chunk, Function<BlockPos, Biome> biomePos, Random random, int seaLevel, int chunkXOffset, int chunkZOffset, int chunkX, int chunkZ, BitSet carvingMask, ProbabilityConfig config) {
        int ravineLength = (this.func_222704_c() * 2 - 1) * 16;
        double x = chunkXOffset * 16 + random.nextInt(16);
//        double y = random.nextInt(random.nextInt(40) + 8) + 20;
        double z = chunkZOffset * 16 + random.nextInt(16);

        double y = findYPos(chunk, random, x, z);
//        if (y == 0) return false;

        float yaw = random.nextFloat() * 6.2831855F;
        float pitch = (random.nextFloat() - 0.5F) * 2.0F / 8.0F;
        float width = 1; // (random.nextFloat() * 2.0F + random.nextFloat());

        int maxLength = ravineLength - random.nextInt(ravineLength / 4);

        carveRavine(chunk, biomePos, random.nextLong(), seaLevel, chunkX, chunkZ, x, y, z, width, yaw, pitch, 0, maxLength, 3.0D, carvingMask);
        return true;
    }

    private void carveRavine(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX, int chunkZ, double x, double y, double z, float width, float yaw, float pitch, int branchStartIndex, int branchCount, double yawMultiplier, BitSet p_227204_21_) {
        Random random = new Random(seed);
        float stretch = 1.0F;

        for(int y1 = 0; y1 < 256; ++y1) {
            if (y1 == 0 || random.nextInt(3) == 0) {
                stretch = 1.0F + random.nextFloat() * random.nextFloat();
            }

            this.heightToHorizontalStretchFactor[y1] = stretch * stretch;
        }

        float yawChange = 0.0F;
        float pitchChange = 0.0F;

        for(int i = branchStartIndex; i < branchCount; i++) {
            double scaledYaw = 1.5D + (double)(MathHelper.sin((float)i * 3.1415927F / (float)branchCount) * width);
            double scaledPitch = scaledYaw * yawMultiplier;

            scaledYaw *= (double)random.nextFloat() * 0.25D + 0.75D;
            scaledPitch *= (double)random.nextFloat() * 0.25D + 0.75D;

            float deltaXZ = MathHelper.cos(pitch);
            float deltaY = MathHelper.sin(pitch);

            x += (double)(MathHelper.cos(yaw) * deltaXZ);
            y += (double)deltaY;
            z += (double)(MathHelper.sin(yaw) * deltaXZ);

            pitch *= 0.7F;
            pitch += pitchChange * 0.05F;
            yaw += yawChange * 0.05F;
            pitchChange *= 0.8F;
            yawChange *= 0.5F;
            pitchChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            yawChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (random.nextInt(4) != 0) {
                if (!this.func_222702_a(chunkX, chunkZ, x, z, i, branchCount, width)) {
                    return;
                }

                this.func_227208_a_(chunk, biomePos, seed, seaLevel, chunkX, chunkZ, x, y, z, scaledYaw, scaledPitch, p_227204_21_);
            }
        }
    }

    @Override
    protected boolean func_222708_a(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ, int y) {
        return (scaledRelativeX * scaledRelativeX + scaledRelativeZ * scaledRelativeZ) * (double) this.heightToHorizontalStretchFactor[y - 1] + scaledRelativeY * scaledRelativeY / 6.0 >= 1.0;
    }

    private double findYPos(IChunk chunk, Random random, double x, double z) {
        for (int yCheck = 0; yCheck < 128; yCheck++) {

            // If there are multiple levels in current part of biome, add some randomness to which level the ravine generates on with a bias towards generating on the bottom levels
            if (random.nextInt(3) != 0) {
                if (chunk.getBlockState(new BlockPos(x, yCheck, z)) == ModBlocks.GLOWDUST_SAND.get().getDefaultState()) {
                    return yCheck - 5;
                }
            }
        }

        return 0;
    }
}
