package com.gmail.leeyi45.PlayerListPlugin.telegramBot;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import org.bukkit.Bukkit;
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

            //Messages that are before start don't process
            if(msg.getDate() < startTime) return;

            String msgText = msg.getText();

            if(msgText.startsWith("/"))
            {
                String[] args = msgText.substring(1).split(" ");

                if(args[0].endsWith("@" + getBotUsername()))
                { //We need to remove the username string
                    args[0] = args[0].substring(0, args[0].length() - getBotUsername().length() - 1);
                }

                PlayerListPlugin.logToConsole(String.format("Telegram Command '%s' received from '%s'", args[0], msg.getFrom().getUserName()), Level.INFO);

                String reply = CommandProcessor.processCommand(msg, args);

                if(!reply.isEmpty() && !reply.isBlank())
                {
                    var send = new SendMessage(msg.getChatId(), reply).enableHtml(true);

                    try { execute(send); }
                    catch(TelegramApiException e)
                    {
                        PlayerListPlugin.logToConsole("Error occurred when replying to telegram message", Level.SEVERE);
                    }
                }
            }
            //Telegram messages sent to server
            else if(chatListening && msg.getFrom().getId() == Config.getTelegramAdmin())
            {
                Bukkit.broadcastMessage("[Server] " + msgText);
            }
        }
    }

    private static BotSession session;
    private static TelegramLongPollingBot bot;
    private static long startTime;

    private static boolean initialized = false;
    public static boolean getInitialized() { return initialized; }

    public static void startThread(CommandSender sender)
    {
        try
        {
            if(initialized) stopBot(sender);
            ApiContextInitializer.init();
        }
        catch(NoClassDefFoundError e)
        {
            sender.sendMessage("Did not locate tele.jar, check lib folder");
            return;
        }

        sender.sendMessage("Loading telegram bot");

        new Thread(() ->
        {
            try
            {
                TelegramBotsApi botApi = new TelegramBotsApi();
                bot = new TelegramMain();
                session = botApi.registerBot(bot);
                initialized = true;
                startTime = System.currentTimeMillis() / 1000;
                sender.sendMessage("Telegram bot running");
            }
            catch(TelegramApiException e)
            {
                sender.sendMessage("Error occurred when trying to connect to the telegram api");
            }
        }).start();
    }

    public static void stopBot(CommandSender sender)
    {
        new Thread(() ->
        {
            if(session != null && session.isRunning())
            {
                sender.sendMessage("Stopping telegram bot");
                session.stop();
            }
            initialized = false;
        }).start();
    }

    private static boolean chatListening = false;

    public static boolean getChatListening() { return chatListening; }
    public static void setChatListening(boolean value) { chatListening = value; }

    //Server messages sent to telegram
    public static void chatListenerMessage(String msg)
    {
        if(initialized)
        {
            var send = new SendMessage(Config.getTelegramAdmin(), msg)
                    .enableHtml(true);

            try { bot.execute(send); }
            catch(TelegramApiException e)
            {
                PlayerListPlugin.logToConsole("TelegramApiException occurred when trying to send chat listener message: "
                        + e.getMessage(), Level.SEVERE);
            }
        }
    }
}