package com.branders.spawnermod.registry;

import com.branders.spawnermod.SpawnerMod;
import com.branders.spawnermod.command.SpawnerModCommands;
import com.branders.spawnermod.item.SpawnerKey;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModRegistry {

    //public static final Item SPAWNER_KEY = new SpawnerKey(new Item.Settings().maxDamage(10).rarity(Rarity.RARE));
    public static final Item SPAWNER_KEY = register(
        "spawner_key",
        SpawnerKey::new,
        new Item.Settings()
            .maxDamage(10)
            .rarity(Rarity.RARE)
    );

    public static void register() {

        // Item groups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(SPAWNER_KEY));

        //Registry.register(Registries.ITEM, Identifier.of(SpawnerMod.MOD_ID, "spawner_key"), SPAWNER_KEY);

        SpawnerModCommands.register();
    }

    /**
     * Cria o RegistryKey, injeta no Settings e registra o item.
     */
    private static <T extends Item> T register(
        String name,
        Function<Item.Settings, T> factory,
        Item.Settings settings
    ) {
        // cria o key para o item
        RegistryKey<Item> key = RegistryKey.<Item>of(
            RegistryKeys.ITEM, 
            Identifier.of(SpawnerMod.MOD_ID, name)
        );

        // instancia o seu Item (a registryKey é injetada no settings)
        T item = factory.apply(settings.registryKey(key));

        // REGISTRA USANDO O IDENTIFIER, não o key
        Registry.register(Registries.ITEM, key.getValue(), item);

        return item;
    }

    /**
     * Given the entity, get its spawn egg registry name. Spawn eggs can have
     * different naming schemes.
     * 
     * @param entityString
     * @return modid:entity_spawn_egg or whackmod_spawn_egg_entity
     */
    public static String getSpawnEggRegistryName(String entityString) {
        Item egg = null;

        // if we follow minecraft naming conventions this will not be null
        egg = Registries.ITEM.get(Identifier.of(entityString + "_spawn_egg"));

        if (egg == null) {
            // entity is "whackmod:pig" and we want it to be "whackmod:spawn_egg_pig"
            String[] split = entityString.split(":");
            assert (split.length == 2);
            String id = split[0];
            String e = "spawn_egg_" + split[1];
            return id + ":" + e;
        }

        return entityString + "_spawn_egg";
    }
}
