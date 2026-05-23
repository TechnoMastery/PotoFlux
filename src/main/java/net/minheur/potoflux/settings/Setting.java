package net.minheur.potoflux.settings;

import net.minheur.potoflux.settings.types.ISettingType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

public record Setting(ResourceLocation id, ISettingType type, boolean requireRestart) {
}
