package com.gmail.leeyi45.PlayerListPlugin.TelegramBot;

import com.gmail.leeyi45.PlayerListPlugin.PluginMain.PlayerListPlugin;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

import java.util.logging.Level;

public class TelegramMain extends TelegramLongPollingBot implements Runnable
{
    @Override
    public String getBotUsername() { return "leeyi45bot";}

    @Override
    public String getBotToken() { return "773814416:AAH78Woebf1JG2D1Rv5c306NYvMDvnQVwA0"; }

    @Override
    public void onUpdateReceived(Update update)
    {
        if(update.hasMessage() && update.getMessage().hasText())
        {
            Message msg = update.getMessage();
            String msgText = msg.getText();

            if(msgText.startsWith("/"))
            {
                String[] args = msgText.substring(1).split(" ");
                PlayerListPlugin.logToConsole(String.format("Command '%s' received from '%s'", args[0], msg.getFrom().getUserName()));
                String reply = CommandProcessor.processCommand(msg, args);

                SendMessage send = new SendMessage()
                        .setText(reply)
                        .enableHtml(true)
                        .setChatId(msg.getChatId());

                try { execute(send); }
                catch(TelegramApiException e)
                {
                    PlayerListPlugin.logToConsole("Error occurred when replying to telegram message", Level.SEVERE);
                }
            }
        }
    }

    private BotSession session;

    private static TelegramMain instance;
    private static boolean initialized = false;

    public static void startThread()
    {
        if(initialized) stopBot();

        PlayerListPlugin.logToConsole("Loading telegram bot");

        ApiContextInitializer.init();
        instance = new TelegramMain();
        new Thread(instance).start();
    }

    public static void stopBot()
    {
        PlayerListPlugin.logToConsole("Stopping telegram bot");
        if(instance.session.isRunning())
        {
            instance.session.stop();
            initialized = false;
        }
    }

    @Override
    public void run()
    {
        try
        {
            TelegramBotsApi bot = new TelegramBotsApi();
            session = bot.registerBot(this);
            initialized = true;
        }
        catch(TelegramApiException e)
        {
            PlayerListPlugin.logToConsole("Error occurred when trying to connect to the telegram api", Level.SEVERE);
        }
        catch(NoClassDefFoundError e)
        {
            PlayerListPlugin.logToConsole("Did not locate tele.jar, check lib folder", Level.SEVERE);
        }
    }
}
