package net.danh.donggop;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Economy econ = null;

    private static final Logger log = Logger.getLogger("Minecraft");

    public static File storage;

    public static FileConfiguration fStore;

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(), (Plugin)this);
        getCommand("cfund").setExecutor(new cfund());
        saveDefaultConfig();
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        storage = new File(getDataFolder(), "fstore.yml");
        if (!storage.exists())
            try {
                Bukkit.getLogger().info(ChatColor.DARK_RED + "Pinfo not found, creating pinfo.yml...");
                storage.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().info(ChatColor.DARK_RED + "Could not create pinfo.yml");
            }
        fStore = (FileConfiguration)YamlConfiguration.loadConfiguration(storage);
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[] { getDescription().getName() }));
            getServer().getPluginManager().disablePlugin((Plugin)this);
            return;
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = (Economy)rsp.getProvider();
        return (econ != null);
    }
}
