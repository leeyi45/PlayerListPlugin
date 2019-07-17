package com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands;

import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.chatManager.ChatListener;
import com.gmail.leeyi45.PlayerListPlugin.util.MessageSender;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import com.gmail.leeyi45.PlayerListPlugin.telegramBot.TelegramMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

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

        if(args.length == 0) return false;

        switch(args[0].toLowerCase())
        {
            case "discord":
            {
                if(commandSender instanceof Player &&
                    !commandSender.hasPermission("com.gmail.leeyi45.playerlist.discord"))
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
                        !commandSender.hasPermission("com.gmail.leeyi45.playerlist.telegram"))
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
                return true;
            }
            /*
            case "nick":
            {
                if (commandSender instanceof Player &&
                        !commandSender.hasPermission("com.gmail.leeyi45.playerlist.nick"))
                {
                    sender.send("You do not have permission to use this command");
                    return true;
                }
                else return nickCommand(args, sender);
            }*/
            default: return false;
        }
    }

    /*
    private boolean nickCommand(String[] args, MessageSender sender)
    {
        if(args.length == 1) sender.send("Usage: /playerlist nick <set|get>");
        else if(args.length >= 2)
        {
            switch (args[2])
            {
                case "set":
                {
                    if(args.length == 2 || args.length == 3) sender.send("Usage: /playerlist nick set <player> <nickname>");
                    else
                    {
                        ChatListener.setNickname(args[2], args[3]);
                    }
                    break;
                }
                case "get":
                {
                    if(args.length == 2) sender.send("Usage: /playerlist nick get <player>");
                    else
                    {
                        ArrayList<String> playerNames = PlayerListPlugin.getPlayerList();

                        if(playerNames.contains(args[2]))
                        {
                            ArrayList<Player> players = PlayerListPlugin.getOnlinePlayers();
                            sender.send(String.format("Player %s has nickname %s", args[2],
                                    ChatListener.getNickname(args[2])));

                        }
                    }
                    break;
                }
                default:
                {
                    sender.send("Usage: /playerlist nick <set|get>");
                    break;
                }
            }
        }
        return true;
    }*/

    private boolean telegramCommand(String[] args, MessageSender sender)
    {
        if(args.length >= 1)
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
                            sender.send("Usage: /playerlist telegram token set <token>");
                            break;
                        }
                        case 4:
                        {
                            if(args[2].equalsIgnoreCase("set"))
                            {
                                sender.send("Setting telegram token to: " + args[3]);
                                Config.setTelegramToken(args[3]);
                            }
                            else sender.send("Usage: /playerlist telegram token set <token>");
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    sender.send("Usage: /playerlist telegram <disconnect|reconnect|token>");
                    break;
                }
            }
            return true;
        }
        else return false;
    }

    private boolean discordCommand(String[] args, MessageSender sender)
    {
        if(args.length >= 1)
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
                            sender.send("Usage: /playerlist discord token set <token>");
                            break;
                        }
                        case 4:
                        {
                            if(args[2].equalsIgnoreCase("set"))
                            {
                                sender.send("Setting discord token to: " + args[3]);
                                Config.setDiscordToken(args[4]);
                            }
                            else sender.send("Usage: /playerlist discord token set <token>");
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    sender.send("Usage: /playerlist discord <disconnect|reconnect|token>");
                    break;
                }
            }
            return true;
        }
        else return false;
    }
}
