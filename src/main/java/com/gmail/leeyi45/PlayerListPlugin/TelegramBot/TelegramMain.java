package com.gmail.leeyi45.PlayerListPlugin.TelegramBot;

import com.gmail.leeyi45.PlayerListPlugin.PluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.util.MessageSender;
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
    public String getBotUsername() { return "leeyi45bot"; }

    @Override
    public String getBotToken() { return Config.getTelegramToken(); }

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
                PlayerListPlugin.logToConsole(String.format("Command '%s' received from '%s'", args[0], msg.getFrom().getUserName()), Level.INFO);

                if(args[0].contains("@" + getBotUsername()))
                { //We need to remove the username string
                    args[0] = args[0].substring(0, getBotUsername().length());
                }

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
    private MessageSender sender;

    private static TelegramMain instance;
    private static boolean initialized = false;

    public static void startThread(MessageSender sender)
    {
        instance.sender = sender;
        if(initialized) stopBot(sender);

        instance.sender.send("Loading telegram bot");

        ApiContextInitializer.init();
        instance = new TelegramMain();
        new Thread(instance).start();
    }

    public static void stopBot(MessageSender sender)
    {
        sender.send("Stopping telegram bot");
        if(instance != null)
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
            sender.send("Telegram bot running");
        }
        catch(TelegramApiException e)
        {
            sender.send("Error occurred when trying to connect to the telegram api");
        }
        catch(NoClassDefFoundError e)
        {
            sender.send("Did not locate tele.jar, check lib folder");
        }
    }
}
