package com.gmail.leeyi45.PlayerListPlugin.telegramBot;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import org.bukkit.Bukkit;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                "help - Displays this command";
    }

    //!status command
    private static String statusCommand()
    {
        //Matcher matcher = Pattern.compile("1\\.(\\d{1,2})(\\.\\d)?").matcher(Bukkit.getServer().getVersion());

        return String.format("Server is currently running version <b>%s</b> at IP address: <b>%s</b>",
                Bukkit.getServer().getVersion(), Config.getIP_String());
    }

    //!players command
    private static String playersCommand()
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
