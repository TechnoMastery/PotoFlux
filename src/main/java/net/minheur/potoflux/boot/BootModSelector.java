package net.minheur.potoflux.boot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class BootModSelector {
    private static final Set<String> ILLEGAL_MOD_IDS = Set.of("potoflux", "file", "common");

    private final String potofluxVersion;
    private final Set<String> availableClasses;
    private final Map<String, BootModMetadata> byId = new HashMap<>();

    BootModSelector(String potofluxVersion, Set<String> availableClasses) {
        this.potofluxVersion = potofluxVersion;
        this.availableClasses = availableClasses;
    }

    List<BootModMetadata> select(List<BootModMetadata> discoveredMods) {
        List<BootModMetadata> candidates = new ArrayList<>();

        for (BootModMetadata mod : discoveredMods) {
            if (mod.modId == null || mod.modId.isBlank()) {
                disable(mod, "missing modId");
            } else if (ILLEGAL_MOD_IDS.contains(mod.modId)) {
                disable(mod, "illegal modId");
            } else if (byId.containsKey(mod.modId)) {
                disable(mod, "duplicate modId");
            } else {
                byId.put(mod.modId, mod);
                candidates.add(mod);
            }
        }

        for (BootModMetadata mod : candidates) resolve(mod, new HashSet<>());
        return discoveredMods;
    }

    private boolean resolve(BootModMetadata mod, Set<String> visiting) {
        if (mod.loadable) return true;
        if (mod.disabledReason != null) return false;

        if (!visiting.add(mod.modId)) {
            disable(mod, "circular dependency");
            return false;
        }

        if (!isCompatibleWithPotoflux(mod)) {
            disable(mod, "incompatible with PotoFlux " + potofluxVersion);
            visiting.remove(mod.modId);
            return false;
        }

        for (String dependencySpec : mod.dependenciesIds) {
            Dependency dependency = Dependency.parse(dependencySpec);
            BootModMetadata dependencyMod = byId.get(dependency.id);

            if (dependencyMod == null) {
                disable(mod, "missing dependency " + dependency.id);
                visiting.remove(mod.modId);
                return false;
            }
            if (!dependency.accepts(dependencyMod.version)) {
                disable(mod, "dependency " + dependency.id + " has wrong version " + dependencyMod.version);
                visiting.remove(mod.modId);
                return false;
            }
            if (!resolve(dependencyMod, visiting)) {
                disable(mod, "dependency " + dependency.id + " is not loadable");
                visiting.remove(mod.modId);
                return false;
            }
        }

        for (String externalDependency : mod.externalDependencies) {
            if (!availableClasses.contains(externalDependency)) {
                disable(mod, "missing external class " + externalDependency);
                visiting.remove(mod.modId);
                return false;
            }
        }

        mod.loadable = true;
        visiting.remove(mod.modId);
        return true;
    }

    private boolean isCompatibleWithPotoflux(BootModMetadata mod) {
        if (mod.compatibleVersions.contains(potofluxVersion)) return true;
        if (!mod.compatibleVersions.contains("-1")) return false;
        if ("NONE".equals(mod.compatibleVersionUrl)) return false;

        try {
            JsonObject root = JsonParser.parseReader(new InputStreamReader(
                    URI.create(mod.compatibleVersionUrl).toURL().openStream(),
                    StandardCharsets.UTF_8
            )).getAsJsonObject();
            JsonObject versions = root.getAsJsonObject("versions");
            if (versions == null) return false;
            JsonObject version = versions.getAsJsonObject(mod.version);
            if (version == null) return false;
            JsonElement compatList = version.get("compatList");
            if (compatList == null || !compatList.isJsonArray()) return false;

            for (JsonElement element : compatList.getAsJsonArray()) {
                if (potofluxVersion.equals(element.getAsString())) return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void disable(BootModMetadata mod, String reason) {
        mod.loadable = false;
        mod.disabledReason = reason;
    }

    private record Dependency(String id, String minVersion, String maxVersion) {
        static Dependency parse(String formatted) {
            String[] parts = formatted.split(":");
            if (parts.length < 2) return new Dependency(formatted, "0", String.valueOf(Integer.MAX_VALUE));
            return new Dependency(parts[0], parts[1], parts.length == 2 ? parts[1] : parts[2]);
        }

        boolean accepts(String version) {
            int actual = Integer.parseInt(version);
            return actual >= Integer.parseInt(minVersion) && actual <= Integer.parseInt(maxVersion);
        }
    }
}
