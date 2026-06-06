package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.settings.SettingRegistry;

/**
 * Event to register settings.
 */
public class RegisterSettingEvent {
    /**
     * Registry containing all settings.
     */
    public final SettingRegistry reg = new SettingRegistry();
}
