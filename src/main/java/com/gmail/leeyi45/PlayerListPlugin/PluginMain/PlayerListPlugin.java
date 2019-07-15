package com.gmail.leeyi45.PlayerListPlugin.PluginMain;

import com.gmail.leeyi45.PlayerListPlugin.PluginMain.Commands.DiscordCommand;
import com.gmail.leeyi45.PlayerListPlugin.PluginMain.Commands.TelegramCommand;
import com.gmail.leeyi45.PlayerListPlugin.PluginMain.Commands.UpdateCommand;
import com.gmail.leeyi45.PlayerListPlugin.DiscordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.TelegramBot.TelegramMain;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerListPlugin extends JavaPlugin
{
    private Logger logger;
    private static ArrayList<String> playerList;
    private static PlayerListPlugin instance;

    public static void logToConsole(String text) { logToConsole(text, Level.INFO); }

    public static void logToConsole(String text, Level level) { instance.logger.log(level, text); }

    public static void setPlayerList(ArrayList<String> list) { playerList = list; }

    public static ArrayList<String> getPlayerList() { return playerList; }

    public static int getPlayerCount() { return playerList.size(); }

    public static void addPlayer(String name) { playerList.add(name); }

    public static void removePlayer(String name) { playerList.remove(name); }

    public static PlayerListPlugin getInstance() { return instance; }

    @Override
    public void onEnable()
    {
        instance = this;
        logger = getLogger();
        playerList = new ArrayList<>();

        logToConsole("Enabling PlayerListPlugin!");

        //Start the bots
        DiscordMain.startThread();
        TelegramMain.startThread();

        //Basic setup
        saveDefaultConfig();
        Config.loadConfig(getConfig());
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
        DiscordMain.stopBot();
        TelegramMain.stopBot();
    }

    private void registerCommands()
    {
        Map<String, CommandExecutor> dict = new HashMap<>();

        dict.put("playerlist-update", new UpdateCommand());
        dict.put("playerlist-discord", new DiscordCommand());
        dict.put("playerlist-tele", new TelegramCommand());

        for(Map.Entry<String, CommandExecutor> item : dict.entrySet())
        {
            try
            {
                getCommand(item.getKey()).setExecutor(item.getValue());
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
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
}
