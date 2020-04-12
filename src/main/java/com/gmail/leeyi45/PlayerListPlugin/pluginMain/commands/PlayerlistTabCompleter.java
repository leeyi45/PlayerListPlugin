package com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class PlayerlistTabCompleter implements TabCompleter
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                                String s, String[] args)
    {
        switch(args.length)
        {
            case 1: return Arrays.asList("discord", "telegram", "ip");
            case 2: return Arrays.asList("status", "disconnect", "reconnect", "token");
            case 3:
            {
                if(args[1].equalsIgnoreCase("token")) return Arrays.asList("set", "get");
                else return null;
            }
            default: return null;
        }
    }
}
