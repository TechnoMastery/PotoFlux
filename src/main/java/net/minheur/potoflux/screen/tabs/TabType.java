package net.minheur.potoflux.screen.tabs;

public class TabType {
    private final String id;
    private final String name;
    private final Class<? extends BaseTab> tabClass;

    public TabType(String id, String name, Class<? extends BaseTab> tabClass) {
        this.id = id;
        this.name = name;
        this.tabClass = tabClass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class<? extends BaseTab> getTabClass() {
        return tabClass;
    }

    public BaseTab createInstance() {
        try {
            return tabClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
