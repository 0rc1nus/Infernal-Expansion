/*
 * Copyright 2022 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infernalstudios.infernalexp.init;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import org.infernalstudios.infernalexp.InfernalExpansion;
import org.infernalstudios.infernalexp.world.gen.carvers.GlowstoneRavineCarver;

import java.util.ArrayList;
import java.util.List;

public class IECarvers {

    public static List<WorldCarver<? extends CarverConfiguration>> carvers = new ArrayList<>();

    // Carvers
    public static final WorldCarver<CanyonCarverConfiguration> GLOWSTONE_RAVINE = registerWorldCarver("glowstone_ravine", new GlowstoneRavineCarver(CanyonCarverConfiguration.CODEC));

    // Configured Carvers
    public static final ConfiguredWorldCarver<CanyonCarverConfiguration> CONFIGURED_GLOWSTONE_RAVINE = registerConfiguredCarver("glowstone_ravine", GLOWSTONE_RAVINE.configured(
        new CanyonCarverConfiguration(0.1f, UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.belowTop(1)), ConstantFloat.of(0.5F), VerticalAnchor.aboveBottom(10), false, CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()), UniformFloat.of(-0.125F, 0.125F), new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75F, 1.0F), TrapezoidFloat.of(0.0F, 6.0F, 2.0F), 3, UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F))));

    @SuppressWarnings("deprecation")
    private static <C extends CarverConfiguration> WorldCarver<C> registerWorldCarver(String registryName, WorldCarver<C> carver) {
        ResourceLocation resourceLocation = new ResourceLocation(InfernalExpansion.MOD_ID, registryName);

        if (Registry.CARVER.keySet().contains(resourceLocation))
            throw new IllegalStateException("World Carver ID: \"" + resourceLocation.toString() + "\" is already in the registry!");

        carver.setRegistryName(resourceLocation);
        carvers.add(carver);

        return carver;
    }

    private static <C extends CarverConfiguration> ConfiguredWorldCarver<C> registerConfiguredCarver(String registryName, ConfiguredWorldCarver<C> configuredCarver) {
        ResourceLocation resourceLocation = new ResourceLocation(InfernalExpansion.MOD_ID, registryName);

        if (BuiltinRegistries.CONFIGURED_FEATURE.keySet().contains(resourceLocation))
            throw new IllegalStateException("Configured Carver ID: \"" + resourceLocation.toString() + "\" is already in the registry!");

        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_CARVER, resourceLocation, configuredCarver);
    }

}
