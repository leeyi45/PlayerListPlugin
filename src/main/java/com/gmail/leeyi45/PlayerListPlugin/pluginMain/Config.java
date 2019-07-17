package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import org.bukkit.configuration.file.FileConfiguration;

public class Config
{
    private static FileConfiguration config;

    public static String discordCommandPrefix() { return config.getString("discord.command-prefix"); }

    public static String getIP_String() { return config.getString("server-ip"); }

    public static String getDiscordToken() { return config.getString("discord.token"); }

    public static void setDiscordToken(String token) { config.set("discord.token", token); }

    public static String getTelegramToken()
    {
        return config.getString("telegram.token");
    }

    public static void setConfig(FileConfiguration cfg) { config = cfg; }

    public static void setTelegramToken(String arg)
    {
        config.set("telegram.token", arg);
    }
}
