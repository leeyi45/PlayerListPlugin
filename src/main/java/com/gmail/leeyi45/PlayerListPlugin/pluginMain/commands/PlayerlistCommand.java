package com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands;

import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.telegramBot.TelegramMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlayerlistCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(args.length == 0) return false;

        switch(args[0].toLowerCase())
        {
            case "discord":
            {
                if(!sender.hasPermission("playerlist.discord"))
                {
                    sender.sendMessage("You do not have permission to use this command");
                    return true;
                }
                else return discordCommand(args, sender);
            }
            case "tele":
            case "telegram":
            {
                if(!sender.hasPermission("playerlist.telegram"))
                {
                    sender.sendMessage("You do not have permission to use this command");
                    return true;
                }
                else return telegramCommand(args, sender);
            }
            case "ip":
            {
                if(!sender.hasPermission("playerlist.ip"))
                {
                    sender.sendMessage("You do not have permission to use this command");
                    return true;
                }

                if(args.length >= 2)
                {
                    switch(args[1].toLowerCase())
                    {
                        case "set":
                        {
                            if(args.length != 3) sender.sendMessage("Usage: /playerlist ip set <ip>");
                            else
                            {
                                Config.setIP_String(args[2]);
                                sender.sendMessage("Set IP address to " + args[2]);
                            }
                            break;
                        }
                        case "get":
                        {
                            sender.sendMessage("Current IP address is " + Config.getIP_String());
                            break;
                        }
                        default:
                        {
                            sender.sendMessage("Usage: /playerlist ip <set|get>");
                            break;
                        }
                    }
                }
                else sender.sendMessage("Usage: /playerlist ip <set|get>");

                return true;
            }
            default: return false;
        }
    }

    private boolean telegramCommand(String[] args, CommandSender sender)
    {
        if(args.length >= 2)
        {
            switch(args[1])
            {
                case "status":
                {
                    sender.sendMessage("The telegram bot is currently " + (TelegramMain.getInitialized() ? "online" : "offline"));
                    break;
                }
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
                            sender.sendMessage("Usage: /playerlist telegram token <set|get>");
                            break;
                        }
                        case 3:
                        {
                            if(args[2].equalsIgnoreCase("get"))
                            {
                                sender.sendMessage("Telegram token is " + Config.getTelegramToken());
                            }
                            else if(args[2].equalsIgnoreCase("set"))
                            {
                                sender.sendMessage("Usage: /playerlist telegram token set <token>");
                            }
                            else sender.sendMessage("Usage: /playerlist telegram token <set|get>");
                            break;
                        }
                        case 4:
                        {
                            if(args[2].equalsIgnoreCase("set"))
                            {
                                sender.sendMessage("Setting telegram token to: " + args[3]);
                                Config.setTelegramToken(args[3]);
                            }
                            else sender.sendMessage("Usage: /playerlist telegram token set <token>");
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    sender.sendMessage("Usage: /playerlist telegram <disconnect|reconnect|token|status>");
                    break;
                }
            }
            return true;
        }
        else return false;
    }

    private boolean discordCommand(String[] args, CommandSender sender)
    {
        if(args.length >= 2)
        {
            switch(args[1])
            {
                case "status":
                {
                    sender.sendMessage("The discord bot is currently " + (DiscordMain.getInitialized() ? "online" : "offline"));
                    break;
                }
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
                            sender.sendMessage("Usage: /playerlist discord token <get|set>");
                            break;
                        }
                        case 3:
                        {
                            if(args[2].equalsIgnoreCase("get"))
                            {
                                sender.sendMessage("Discord token is " + Config.getDiscordToken());
                            }
                            else if(args[2].equalsIgnoreCase("set"))
                            {
                                sender.sendMessage("Usage: /playerlist discord token set <token>");
                            }
                            else sender.sendMessage("Usage: /playerlist discord token <get|set>");
                            break;
                        }
                        case 4:
                        {
                            if(args[2].equalsIgnoreCase("set"))
                            {
                                sender.sendMessage("Setting discord token to: " + args[3]);
                                Config.setDiscordToken(args[3]);
                            }
                            else sender.sendMessage("Usage: /playerlist discord token set <token>");
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    sender.sendMessage("Usage: /playerlist discord <disconnect|reconnect|token|status>");
                    break;
                }
            }
            return true;
        }
        else return false;
    }
}
