package com.gmail.leeyi45.PlayerListPlugin.PluginMain.Commands;

import com.gmail.leeyi45.PlayerListPlugin.TelegramBot.TelegramMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TelegramCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {

        if(args.length == 1)
        {
            switch(args[0])
            {
                case "reconnect":
                {
                    TelegramMain.startThread();
                    return true;
                }
                case "disconnect":
                {
                    TelegramMain.stopBot();
                    return true;
                }
                default: return false;
            }
        }
        return false;
    }
}
