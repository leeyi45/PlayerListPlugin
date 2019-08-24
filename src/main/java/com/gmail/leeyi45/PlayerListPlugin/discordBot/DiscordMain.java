package com.gmail.leeyi45.PlayerListPlugin.discordBot;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.managers.Presence;
import org.bukkit.command.CommandSender;

public class DiscordMain
{
    private static JDA bot;
    private static boolean initialized = false;

    public static boolean getInitialized() { return initialized; }

    public static void startThread(CommandSender sender)
    {
        if(initialized)
        { //if the bot is already on
            bot.shutdown();
        }

        new Thread(() ->
        {
            try
            {
                String token = Config.getDiscordToken();
                initialized = false;
                sender.sendMessage("Beginning discord bot initialization using " + token);
                bot = new JDABuilder(token).build();

                bot.addEventListener(new MessageListener());

                sender.sendMessage("Discord bot initialized");
                initialized = true;

                updateBot(sender);
            }
            catch(javax.security.auth.login.LoginException e)
            {
                sender.sendMessage("Login exception when setting up the discord bot");
            }
            catch(NoClassDefFoundError e)
            {
                sender.sendMessage("Did not locate JDA.jar, check lib folder");
            }
        }).start();
    }

    public static void stopBot(CommandSender sender)
    {
        if(bot != null)
        {
            sender.sendMessage("Shutting discord bot down");
            bot.shutdown();
        }
    }

    public static void updateBot(CommandSender sender)
    {
        if(!initialized) return;

        sender.sendMessage("Updating discord bot player count status");

        int count = PlayerListPlugin.getPlayerCount();
        Presence presence = bot.getPresence();

        if(count == 0)
        {
            presence.setGame(Game.of(Game.GameType.DEFAULT,"with nobody"));
            presence.setStatus(OnlineStatus.IDLE);
        }
        else
        {
            presence.setGame(Game.of(Game.GameType.DEFAULT, String.format("with %d player%s", count, count == 1 ? "" : "s")));
            presence.setStatus(OnlineStatus.ONLINE);
        }
    }
}
