package com.gmail.leeyi45.PlayerListPlugin.DiscordBot;

import com.gmail.leeyi45.PlayerListPlugin.PluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.PluginMain.PlayerListPlugin;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.managers.Presence;
import java.util.logging.Level;

public class DiscordMain implements Runnable
{
    private static JDA bot;
    private static Boolean initialized = false;
    public static Boolean getInitialized() { return initialized; }

    public static void startThread()
    {
        if(initialized)
        { //if the bot is already on
            bot.shutdown();
        }
        new Thread(new DiscordMain()).start();
    }

    public static void stopBot()
    {
        PlayerListPlugin.logToConsole("Shutting discord bot down");
        bot.shutdown();
    }

    public static void updateBot()
    {
        if(!initialized) return;

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

    @Override
    public void run()
    {
        try
        {
            String token = Config.getDiscordToken();
            initialized = false;
            PlayerListPlugin.logToConsole("Beginning discord bot initialization using " + token);
            bot = new JDABuilder(token).build();

            registerListeners();

            PlayerListPlugin.logToConsole("Discord bot initialized");
            initialized = true;

            updateBot();
        }
        catch(javax.security.auth.login.LoginException e)
        {
            PlayerListPlugin.logToConsole("Login exception when setting up the discord bot", Level.SEVERE);
        }
        catch(NoClassDefFoundError e)
        {
            PlayerListPlugin.logToConsole("Did not locate JDA.jar, check lib folder", Level.SEVERE);
        }
    }

    private void registerListeners()
    {
        bot.addEventListener(new MessageListener());
    }
}
