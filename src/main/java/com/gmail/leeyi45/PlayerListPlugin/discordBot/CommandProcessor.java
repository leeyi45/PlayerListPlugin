package com.gmail.leeyi45.PlayerListPlugin.discordBot;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import net.dv8tion.jda.core.entities.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

class CommandProcessor
{
    static String processCommand(Message msg, String[] args)
    {
        switch(args[0])
        {
            case "status": return statusCommand();
            case "players": return playersCommand();
            case "help": return helpCommand();
            default: return String.format("Unknown command '%s'", args[0]);
        }
    }

    //!help command
    private static String helpCommand()
    {
        return "status - Displays the server status\n" +
                "players - Lists online players\n" +
                "help - Displays this command\n";
    }

    //!status command
    private static String statusCommand()
    {
        return String.format("Server is currently running version %s at IP address: %s",
                PlayerListPlugin.getVersionString(), Config.getIP_String());
    }

    //!players command
    private static String playersCommand()
    {
        Collection<? extends Player> players = PlayerListPlugin.getPlayerList();

        if(players.size() == 0) return "There are no players on the server";
        else
        {
            StringBuilder outputStr = new StringBuilder("Players on the server:\n\n");

            int i = 1;
            for (Player player : players)
            {
                outputStr.append(String.format("%d. %s\n", i, player.getDisplayName()));
                i++;
            }

            return outputStr.toString();
        }
    }
}
