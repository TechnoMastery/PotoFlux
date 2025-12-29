package net.minheur.potoflux.theme;

import java.util.HashMap;
import java.util.Map;

public class ThemeMap {
    private final Map<Themes, Map<String, Object>> map = new HashMap<>();

    public void add(Themes t, String key, Object o) {
        Map<String, Object> content = map.get(t);

        content.put(key, o);
    }

    public Object get(String key) {
        Map<String, Object> content = map.get(ThemeHandler.getTheme());

        return content.get(key);
    }
}
