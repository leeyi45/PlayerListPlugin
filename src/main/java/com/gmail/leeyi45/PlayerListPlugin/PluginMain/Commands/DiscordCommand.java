package com.gmail.leeyi45.PlayerListPlugin.PluginMain.Commands;

import com.gmail.leeyi45.PlayerListPlugin.DiscordBot.DiscordMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscordCommand implements CommandExecutor
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
                    DiscordMain.startThread();
                    return true;
                }
                case "disconnect":
                {
                    DiscordMain.stopBot();
                    return true;
                }
                default: return false;
            }
        }
        return false;
    }
}
