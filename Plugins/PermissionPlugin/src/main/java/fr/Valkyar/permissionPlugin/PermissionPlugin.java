package fr.Valkyar.permissionPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class PermissionPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("[PermissionPlugin] - PermissionPlugin plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
