package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands.PlayerlistCommand;
import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.telegramBot.TelegramMain;
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

    public static ArrayList<String> getPlayerList()
    {
        var output = new ArrayList<String>();
        for(Player p : instance.getServer().getOnlinePlayers())
        {
            output.add(p.getDisplayName());
        }
        return output;
    }

    public static int getPlayerCount() { return playerList.size(); }

    public static PlayerListPlugin getInstance() { return instance; }

    public static ConsoleCommandSender getConsoleSender() { return instance.getServer().getConsoleSender(); }

    @Override
    public void onEnable()
    {
        instance = this;

        logger = getLogger();
        playerList = new ArrayList<>();

        logToConsole("Enabling PlayerListPlugin!", Level.INFO);

        //Basic setup
        Config.setConfig(getConfig());

        //Start the bots
        DiscordMain.startThread(getConsoleSender());
        TelegramMain.startThread(getConsoleSender());

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
        /*
        var dict = new HashMap<String, CommandExecutor>();

        dict.put("playerlist", new PlayerlistCommand());

        for(var item : dict.entrySet())
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
        }*/
        try
        {
            getCommand("playerlist").setExecutor(new PlayerlistCommand());
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
