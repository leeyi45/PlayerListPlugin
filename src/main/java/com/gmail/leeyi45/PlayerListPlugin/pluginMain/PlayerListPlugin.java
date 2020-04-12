package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands.PlayerlistCommand;
import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands.PlayerlistTabCompleter;
import com.gmail.leeyi45.PlayerListPlugin.telegramBot.TelegramMain;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerListPlugin extends JavaPlugin
{
    private Logger logger;
    private String versionString;
    private static PlayerListPlugin instance;

    public static void logToConsole(String text, Level level) { instance.logger.log(level, text); }

    public static Collection<? extends Player> getPlayerList() { return instance.getServer().getOnlinePlayers(); }

    public static int getPlayerCount() { return getPlayerList().size(); }

    public static ConsoleCommandSender getConsoleSender() { return instance.getServer().getConsoleSender(); }

    public static String getVersionString() { return instance.versionString; }

    @Override
    public void onEnable()
    {
        instance = this;

        //Matcher matcher = Pattern.compile("(1\\.\\d{1,2}(\\.\\d)?)").matcher(Bukkit.getServer().getVersion());
        versionString = Bukkit.getServer().getVersion();

        logger = getLogger();

        //Basic setup
        Config.setConfig(getConfig());

        registerCommands();
        registerEvents();

        //Start the bots
        DiscordMain.startThread(getConsoleSender());
        TelegramMain.startThread(getConsoleSender());
        TelegramMain.onServerStart();
    }

    @Override
    public void onDisable()
    {
        //Don't forget to stop the bots before we shut the plugin down
        DiscordMain.stopBot(getConsoleSender());
        TelegramMain.stopBot(getConsoleSender());

        try { Config.configSave(); }
        catch(IOException e)
        {
            logToConsole("Failed to save config file: " + e.getMessage(), Level.SEVERE);
        }
    }

    private void registerCommands()
    {
        try
        {
            getCommand("playerlist").setExecutor(new PlayerlistCommand());
            getCommand("playerlist").setTabCompleter(new PlayerlistTabCompleter());
        }
        catch(NullPointerException e)
        {
            logToConsole("NullPointerException occurred when registering playerlist command", Level.SEVERE);
        }
    }

    private void registerEvents()
    {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(), this);
    }
}
