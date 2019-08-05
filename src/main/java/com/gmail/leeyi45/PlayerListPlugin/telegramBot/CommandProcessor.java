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
            case "chat": return chatListenerCommand(msg, args);
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
        //Matcher matcher = Pattern.compile("1\\.(\\d{1,2})(\\.\\d)?").matcher();

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

    //!chat command
    private static String chatListenerCommand(Message msg, String[] args)
    {
        if(args.length > 1)
        {
            if (msg.getFrom().getId() == Config.getTelegramAdmin())
            {
                switch (args[1].toLowerCase())
                {
                    case "toggle":
                        TelegramMain.setChatListening(!TelegramMain.getChatListening());
                    case "status":
                        return "<b>Chat listening is " + (TelegramMain.getChatListening() ? "active" : "inactive") + "</b>";
                    default:
                        return String.format("Unknown argument '%s'", args[0]);
                }
            }
            else return "You do not have permission to use this command";
        }
        else return "Usage: /chat {toggle|status}";
    }
}