package com.gmail.leeyi45.PlayerListPlugin.PluginMain;

import org.bukkit.configuration.file.FileConfiguration;

public class Config
{
    private static String file_path;
    private static String discordCommandPrefix;
    private static String IP_String;
    private static String discordToken;

    public static String getFilePath() { return file_path; }

    public static String discordCommandPrefix() { return discordCommandPrefix; }

    public static String getIP_String() { return IP_String; }

    public static String getDiscordToken() { return discordToken; }

    public static void loadConfig(FileConfiguration config)
    {
        file_path = config.getString("file-path");
        discordCommandPrefix = config.getString("discord.command-prefix");
        IP_String = config.getString("server-ip");
        discordToken = config.getString("discord.token");
    }
}
