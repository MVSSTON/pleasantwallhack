package me.mstn.api.nms;

public enum NMSPackage {

    NMS("net.minecraft.server." + Version.getCurrent()),
    CB("org.bukkit.craftbukkit." + Version.getCurrent());

    private final String path;

    NMSPackage(String path) {
        this.path = path;
    }

    NMSPackage(NMSPackage path, String subpath) {
        this(path + "." + subpath);
    }

    public String getPath() {
        return path;
    }

    public Class<?> getClass(String className) {
        try {
            return Class.forName(this + "." + className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return path;
    }

}