package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

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

    public static void configSave() throws IOException
    {
        if(changesMade)
        {
            config.save(config.getCurrentPath());
            changesMade = false;
        }
    }
}
