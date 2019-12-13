package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;

public class Config
{
    private static FileConfiguration config;
    private static boolean changesMade = false;

    public static String discordCommandPrefix() { return config.getString("discord.command-prefix", "!"); }

    public static String getIP_String() { return config.getString("server-ip"); }

    public static void setIP_String(String str)
    {
        config.set("server-ip", str);
        changesMade = true;
    }

    public static String getDiscordToken() { return config.getString("discord.token"); }

    public static void setDiscordToken(String token)
    {
        config.set("discord.token", token);
        changesMade = true;
    }

    public static String getTelegramToken()
    {
        return config.getString("telegram.token");
    }

    public static void setConfig(FileConfiguration cfg) { config = cfg; }

    public static void setTelegramToken(String arg)
    {
        config.set("telegram.token", arg);
        changesMade = true;
    }

    public static long getTelegramAdmin() { return config.getLong("telegram.admin", -1); }

    public static ArrayList<Long> getTelegramNotifys()
    {
        if(!config.contains("telegram.notifys")) return new ArrayList<>();
        var notifySection = config.getConfigurationSection("telegram.notifys");

        var list = new ArrayList<Long>();

        for(var each : notifySection.getValues(false).entrySet())
        {
            if((int)each.getValue() == 1) list.add(Long.parseLong(each.getKey()));
        }
        return list;
    }

    public static void addTelegramNotify(long id)
    {
        var notifySection = config.contains("telegram.notifys") ?
                config.getConfigurationSection("telegram.notifys") : config.createSection("telegram.notifys");
        notifySection.set(String.valueOf(id), 1);
        changesMade = true;
    }

    public static void removeTelegramNotify(long id)
    {
        var notifySection = config.getConfigurationSection("telegram.notifys");
        notifySection.set(String.valueOf(id), 0);
        changesMade = true;
    }

    public static void configSave() throws IOException
    {
        if(changesMade)
        {
            config.save(config.getCurrentPath());
            changesMade = false;
        }
    }
}
