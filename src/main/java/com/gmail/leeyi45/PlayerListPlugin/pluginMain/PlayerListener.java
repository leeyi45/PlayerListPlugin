package com.gmail.leeyi45.PlayerListPlugin.pluginMain;

import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.telegramBot.TelegramMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        DiscordMain.updateBot(PlayerListPlugin.getConsoleSender());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        DiscordMain.updateBot(PlayerListPlugin.getConsoleSender());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        if(TelegramMain.getChatListening())
        {
            TelegramMain.chatListenerMessage(e.getMessage());
        }
    }
}
