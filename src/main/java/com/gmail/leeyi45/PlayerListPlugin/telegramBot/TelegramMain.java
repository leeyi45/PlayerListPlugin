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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            //if(msg.getDate() < startTime) return;

            String msgText = msg.getText();

            if(msgText.startsWith("/"))
            {
                String[] args = msgText.substring(1).split(" ");

                if(args[0].contains("@" + getBotUsername()))
                { //We need to remove the username string
                    Matcher matcher = Pattern.compile(".+?(?=@)").matcher(args[0]);
                    args[0] = matcher.group(1);
                }

                PlayerListPlugin.logToConsole(String.format("Command '%s' received from '%s'", args[0], msg.getFrom().getUserName()), Level.INFO);

                SendMessage send = new SendMessage()
                        .setText(CommandProcessor.processCommand(msg, args))
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
                TelegramBotsApi bot = new TelegramBotsApi();
                session = bot.registerBot(new TelegramMain());
                initialized = true;
                startTime = System.currentTimeMillis();
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
        sender.sendMessage("Stopping telegram bot");
        new Thread(() ->
        {
            if(session != null)
            {
                session.stop();
                initialized = false;
            }
        }).start();
    }
}
