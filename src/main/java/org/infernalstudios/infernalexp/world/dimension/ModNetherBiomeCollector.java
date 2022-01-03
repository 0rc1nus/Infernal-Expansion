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

package org.infernalstudios.infernalexp.world.dimension;

import org.infernalstudios.infernalexp.config.InfernalExpansionConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ModNetherBiomeCollector {
    public static List<ResourceKey<Biome>> netherBiomeList = new ArrayList<>();
    public static List<String> biomeList = Arrays.asList(((String) InfernalExpansionConfig.WorldGeneration.BIOMES_LIST.get()).replace(" ", "").split(","));
    public static boolean isWhitelist = (Boolean) InfernalExpansionConfig.WorldGeneration.BIOMES_LIST_IS_WHITELIST.get();

    public static List<ResourceKey<Biome>> netherBiomeCollection(Registry<Biome> biomeRegistry) {

        for (Map.Entry<ResourceKey<Biome>, Biome> entry : biomeRegistry.entrySet()) {
            if (entry.getValue().getBiomeCategory() == Biome.BiomeCategory.NETHER && !entry.getKey().location().getNamespace().equals("ultra_amplified_dimension")) {
                if (!netherBiomeList.contains(entry.getKey())) {
                    if ((isWhitelist && biomeList.contains(entry.getKey().location().toString()))
                        || (!isWhitelist && !biomeList.contains(entry.getKey().location().toString()))
                        || biomeList.isEmpty()) {

                        netherBiomeList.add(entry.getKey());

                    }
                }
            }
        }

        netherBiomeList.sort(Comparator.comparing(key -> key.location().toString()));
        return netherBiomeList;
    }

    public static int getRandomNetherBiomes(Context random, Registry<Biome> registry) {
        return registry.getId(registry.get(netherBiomeList.get(random.nextRandom(netherBiomeList.size()))));
    }
}
