package com.gmail.leeyi45.PlayerListPlugin.PluginMain.Commands;

import com.gmail.leeyi45.PlayerListPlugin.DiscordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.PluginMain.PlayerListPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UpdateCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        PlayerListPlugin.logToConsole("Updating the internal player list");
        ArrayList<String> newList = new ArrayList<>();
        for (Player player: PlayerListPlugin.getInstance().getServer().getOnlinePlayers())
        {
            newList.add(player.getDisplayName());
        }

        PlayerListPlugin.setPlayerList(newList);
        DiscordMain.updateBot();
        return true;
    }
}
