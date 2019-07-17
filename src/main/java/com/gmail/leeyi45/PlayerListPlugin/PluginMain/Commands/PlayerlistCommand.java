package com.gmail.leeyi45.PlayerListPlugin.PluginMain.Commands;

import com.gmail.leeyi45.PlayerListPlugin.DiscordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.PluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.util.MessageSender;
import com.gmail.leeyi45.PlayerListPlugin.PluginMain.PlayerListPlugin;
import com.gmail.leeyi45.PlayerListPlugin.TelegramBot.TelegramMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerlistCommand implements CommandExecutor
{
    private CommandSender commandSenderMain;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        commandSenderMain = commandSender;
        MessageSender sender = new MessageSender()
        {
            @Override
            public void send(String send)
            {
                commandSenderMain.sendMessage(send);
            }
        };


        switch(args[0])
        {
            case "discord":
            {
                if(commandSender instanceof Player &&
                    commandSender.hasPermission("com.gmail.leeyi45.playerlist.discord"))
                {
                    sender.send("You do not have permission to use this command");
                    return true;
                }
                else return discordCommand(args, sender);
            }
            case "tele":
            case "telegram":
            {
                if(commandSender instanceof Player &&
                        commandSender.hasPermission("com.gmail.leeyi45.playerlist.telegram"))
                {
                    sender.send("You do not have permission to use this command");
                    return true;
                }
                else return telegramCommand(args, sender);
            }
            case "update":
            {
                sender.send("Updating the internal player list");

                ArrayList<String> newList = new ArrayList<>();
                for (Player player: PlayerListPlugin.getInstance().getServer().getOnlinePlayers())
                {
                    newList.add(player.getDisplayName());
                }

                PlayerListPlugin.setPlayerList(newList);
                DiscordMain.updateBot();
            }
            default: return false;
        }
    }

    private boolean telegramCommand(String[] args, MessageSender sender)
    {
        if(args.length >= 2)
        {
            switch(args[1])
            {
                case "reconnect":
                {
                    TelegramMain.stopBot(sender);
                    TelegramMain.startThread(sender);
                    break;
                }
                case "disconnect":
                {
                    TelegramMain.stopBot(sender);
                    break;
                }
                case "token":
                {
                    switch(args.length)
                    {
                        case 2:
                        {
                            sender.send("Telegram token is " + Config.getTelegramToken());
                            break;
                        }
                        case 3:
                        {
                            sender.send("Usage: playerlist telegram token set <token>");
                            break;
                        }
                        case 4:
                        {
                            if(args[3].equalsIgnoreCase("set"))
                            {
                                sender.send("Setting telegram token to: " + args[4]);
                                Config.setTelegramToken(args[4]);
                            }
                            else sender.send("Usage: playerlist telegram token set <token>");
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    sender.send("Usage: playerlist telegram {disconnect|reconnect|token}");
                    break;
                }
            }
            return true;
        }
        else return false;
    }

    private boolean discordCommand(String[] args, MessageSender sender)
    {
        if(args.length >= 2)
        {
            switch(args[1])
            {
                case "reconnect":
                {
                    DiscordMain.stopBot(sender);
                    DiscordMain.startThread(sender);
                    break;
                }
                case "disconnect":
                {
                    DiscordMain.stopBot(sender);
                    break;
                }
                case "token":
                {
                    switch(args.length)
                    {
                        case 2:
                        {
                            sender.send("Discord token is " + Config.getDiscordToken());
                            break;
                        }
                        case 3:
                        {
                            sender.send("Usage: playerlist discord token set <token>");
                            break;
                        }
                        case 4:
                        {
                            if(args[3].equalsIgnoreCase("set"))
                            {
                                sender.send("Setting discord token to: " + args[4]);
                                Config.setDiscordToken(args[4]);
                            }
                            else sender.send("Usage: playerlist discord token set <token>");
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    sender.send("Usage: playerlist discord {disconnect|reconnect|token}");
                    break;
                }
            }
            return true;
        }
        else return false;
    }
}
