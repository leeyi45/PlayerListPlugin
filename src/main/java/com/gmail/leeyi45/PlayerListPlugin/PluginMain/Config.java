package com.gmail.leeyi45.PlayerListPlugin.PluginMain;

import org.bukkit.configuration.file.FileConfiguration;

public class Config
{
    private static String discordCommandPrefix;
    private static String IP_String;
    private static String discordToken;
    private static String telegramToken;

    public static String discordCommandPrefix() { return discordCommandPrefix; }

    public static String getIP_String() { return IP_String; }

    public static String getDiscordToken() { return discordToken; }

    public static void loadConfig(FileConfiguration config)
    {
        discordCommandPrefix = config.getString("discord.command-prefix");
        IP_String = config.getString("server-ip");
        discordToken = config.getString("discord.token");
        telegramToken = config.getString("telegram.token");
    }

    public static String getTelegramToken()
    {
        return telegramToken;
    }
}
