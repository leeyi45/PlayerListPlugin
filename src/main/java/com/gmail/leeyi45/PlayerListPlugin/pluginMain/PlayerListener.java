package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        PlayerListPlugin.addPlayer(event.getPlayer().getDisplayName());
        DiscordMain.updateBot();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        PlayerListPlugin.removePlayer(event.getPlayer().getDisplayName());
        DiscordMain.updateBot();
    }
}
