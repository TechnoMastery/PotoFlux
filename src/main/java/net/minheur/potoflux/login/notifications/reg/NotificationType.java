package net.minheur.potoflux.login.notifications.reg;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * Notification type record.
 */
public record NotificationType(ResourceLocation id, Class<? extends INotificationType> clazz) implements IRegistryType {
}
