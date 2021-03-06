package me.bristermitten.spigotexamples;

import me.bristermitten.spigotexamples.config_di.ConfigWrapper;
import me.bristermitten.spigotexamples.config_di.ReloadConfigCommand;
import me.bristermitten.spigotexamples.custom_enchantment.EnchantmentManager;
import me.bristermitten.spigotexamples.runtimelibraries.Library;
import me.bristermitten.spigotexamples.runtimelibraries.LibraryManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotExamples extends JavaPlugin
{

    private static final String KOTLIN_VERSION = "1.3.72";
    private EnchantmentManager enchantmentManager;

    private ConfigWrapper configWrapper;

    @Override
    public void onEnable()
    {
        registerEnchantments();
        loadLibraries();
        loadConfig();
        registerCommands();
    }

    private void registerCommands() {
        final PluginCommand command = getCommand("reloadspigotexamples");
        if(command != null )
        {
            command.setExecutor(new ReloadConfigCommand(this, configWrapper));
        }
    }
    private void loadConfig() {
        saveDefaultConfig();
        this.configWrapper = new ConfigWrapper(getConfig());
    }

    private void loadLibraries()
    {
        LibraryManager libraryManager = new LibraryManager(this);

        //For this example we will download the Kotlin stdlib
        libraryManager.loadLibraries(
                new Library("org.jetbrains.kotlin", "kotlin-stdlib", KOTLIN_VERSION),
                new Library("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", KOTLIN_VERSION)
        );

        try
        {
            Class.forName("kotlin.Unit");
        }
        catch (ClassNotFoundException e)
        {
            getSLF4JLogger().error("Kotlin stdlib was not loaded! Runtime Libraries didn't work?", e);
        }
    }

    private void registerEnchantments()
    {
        this.enchantmentManager = new EnchantmentManager(this);
        enchantmentManager.registerAllCustomEnchantments();
    }

    public EnchantmentManager getEnchantmentManager()
    {
        return enchantmentManager;
    }

    @Override
    public void onDisable()
    {
        enchantmentManager.unregisterAllCustomEnchantments();
    }
}
