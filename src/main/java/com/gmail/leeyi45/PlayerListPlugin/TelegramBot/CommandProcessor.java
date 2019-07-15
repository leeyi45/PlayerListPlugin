package com.gmail.leeyi45.PlayerListPlugin.TelegramBot;

import com.gmail.leeyi45.PlayerListPlugin.PluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.PluginMain.PlayerListPlugin;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

public class CommandProcessor
{
    public static String processCommand(Message msg, String[] args)
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
    static String helpCommand()
    {
        return "status - Displays the server status" +
                "players - Lists online players" +
                "help - Displays this command";
    }

    //!status command
    static String statusCommand()
    {
        return String.format("<b>Server is currently running %s at %s</b>", "1.14.2", Config.getIP_String());
    }

    //!players command
    static String playersCommand()
    {
        ArrayList<String> players = PlayerListPlugin.getPlayerList();

        if(players.size() == 0) return "There are no players on the server";
        else
        {
            StringBuilder outputStr = new StringBuilder("<b>Players on the server:</b>\n\n");

            int i = 1;
            for (String str : players)
            {
                outputStr.append(String.format("%d. %s\n", i, str));
                i++;
            }

            return outputStr.toString();
        }
    }
}