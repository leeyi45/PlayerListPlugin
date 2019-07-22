package com.gmail.leeyi45.PlayerListPlugin.pluginMain.chatManager;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatListener implements Listener
{
    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        String msg = e.getMessage();

        msg = ChatColor.translateAlternateColorCodes('&', msg);
        /*If players have permission to use colour chat it gives it to them
        if(player.hasPermission("playerlist.colorchat"))
        {
            maybe for some implementation in the future
        }*/

        e.setMessage(msg);
    }

    @EventHandler
    public void onServerCommandEvent(ServerCommandEvent event)
    {
        String cmd = event.getCommand();
        String[] args = cmd.split(" ");

        if(args[0].equalsIgnoreCase("say"))
        {
            event.setCommand(ChatColor.translateAlternateColorCodes('&', cmd));
        }
    }

    /*
    public static String getNickname(UUID player)
    {


        return player;
    }*/

    public static boolean setNickname(String name, String nick)
    {
        ArrayList<Player> players = PlayerListPlugin.getOnlinePlayers();
        ArrayList<String> playerNames = PlayerListPlugin.getPlayerList();

        if(playerNames.contains(name)) {

            return true;
        }
        else return false;
    }
}
