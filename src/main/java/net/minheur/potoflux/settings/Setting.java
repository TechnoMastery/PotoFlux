package net.minheur.potoflux.settings;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.settings.types.ISettingType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * Container for a setting
 * @param id of the setting
 * @param type of the setting stored in the prefs
 * @param requireRestart if the app needs to be relaunched to apply the setting
 */
public record Setting(ResourceLocation id, ISettingType<?> type, boolean requireRestart) implements IRegistryType {
}
