package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands.PlayerlistCommand;
import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.chatManager.ChatListener;
import com.gmail.leeyi45.PlayerListPlugin.telegramBot.TelegramMain;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerListPlugin extends JavaPlugin
{
    private Logger logger;
    private static ArrayList<String> playerList;
    private static PlayerListPlugin instance;

    public static void logToConsole(String text, Level level) { instance.logger.log(level, text); }

    public static void setPlayerList(ArrayList<String> list) { playerList = list; }

    public static ArrayList<String> getPlayerList() { return playerList; }

    public static ArrayList<Player> getOnlinePlayers() { return (ArrayList<Player>)instance.getServer().getOnlinePlayers(); }

    public static int getPlayerCount() { return playerList.size(); }

    public static void addPlayer(String name) { playerList.add(name); }

    public static void removePlayer(String name) { playerList.remove(name); }

    public static PlayerListPlugin getInstance() { return instance; }

    public static ConsoleCommandSender getConsoleSender() { return instance.getServer().getConsoleSender(); }

    @Override
    public void onEnable()
    {
        instance = this;

        logger = getLogger();
        playerList = new ArrayList<>();

        logToConsole("Enabling PlayerListPlugin!", Level.INFO);

        //Start the bots
        DiscordMain.startThread(getConsoleSender());
        TelegramMain.startThread(getConsoleSender());

        //Basic setup
        Config.setConfig(getConfig());
        registerCommands();
        registerEvents();

        //We add all players to the list
        for (Player player: PlayerListPlugin.getInstance().getServer().getOnlinePlayers())
        {
            playerList.add(player.getDisplayName());
        }
    }

    @Override
    public void onDisable()
    {
        //Don't forget to stop the bots before we shut the plugin down
        DiscordMain.stopBot(getConsoleSender());
        TelegramMain.stopBot(getConsoleSender());
    }

    private void registerCommands()
    {
        Map<String, CommandExecutor> dict = new HashMap<>();

        dict.put("playerlist", new PlayerlistCommand());

        for(Map.Entry<String, CommandExecutor> item : dict.entrySet())
        {
            try
            {
                //getCommand(item.getKey()).setExecutor(item.getValue());
                getCommand("playerlist").setExecutor(new PlayerlistCommand());
            }
            catch(NullPointerException e)
            {
                logToConsole(String.format("Error occurred when registering command '%s' check plugin yml",
                        item.getKey()), Level.SEVERE);
            }
        }
    }

    private void registerEvents()
    {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new ChatListener(), this);
    }
}
