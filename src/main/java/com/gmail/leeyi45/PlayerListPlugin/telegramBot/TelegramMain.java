package com.gmail.leeyi45.PlayerListPlugin.telegramBot;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import org.bukkit.command.CommandSender;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

import java.util.logging.Level;

public class TelegramMain extends TelegramLongPollingBot
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
                    args[0] = args[0].substring(0, getBotUsername().length() - 4);
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

    private static BotSession session;
    private static CommandSender cmdSender;

    private static boolean initialized = false;

    public static void startThread(CommandSender sender)
    {
        ApiContextInitializer.init();
        cmdSender = sender;
        if(initialized) stopBot(sender);

        sender.sendMessage("Loading telegram bot");

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    TelegramBotsApi bot = new TelegramBotsApi();
                    session = bot.registerBot(new TelegramMain());
                    initialized = true;
                    cmdSender.sendMessage("Telegram bot running");
                }
                catch(TelegramApiException e)
                {
                    cmdSender.sendMessage("Error occurred when trying to connect to the telegram api");
                }
                catch(NoClassDefFoundError e)
                {
                    cmdSender.sendMessage("Did not locate tele.jar, check lib folder");
                }
            }
        }).start();
    }

    public static void stopBot(CommandSender sender)
    {
        sender.sendMessage("Stopping telegram bot");
        new Thread(new Runnable() {
           @Override
           public void run()
           {
               if(session != null)
               {
                   session.stop();
                   initialized = false;
               }
           }
        }).start();
    }
}
