package com.gmail.leeyi45.PlayerListPlugin.pluginMain.commands;

import com.gmail.leeyi45.PlayerListPlugin.discordBot.DiscordMain;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import com.gmail.leeyi45.PlayerListPlugin.telegramBot.TelegramMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
                if(sender instanceof Player &&
                    !sender.hasPermission("playerlist.discord"))
                {
                    sender.sendMessage("You do not have permission to use this command");
                    return true;
                }
                else return discordCommand(args, sender);
            }
            case "tele":
            case "telegram":
            {
                if(sender instanceof Player &&
                        !sender.hasPermission("playerlist.telegram"))
                {
                    sender.sendMessage("You do not have permission to use this command");
                    return true;
                }
                else return telegramCommand(args, sender);
            }
            case "update":
            {
                sender.sendMessage("Updating the internal player list");

                ArrayList<String> newList = new ArrayList<>();
                for (Player player: PlayerListPlugin.getInstance().getServer().getOnlinePlayers())
                {
                    newList.add(player.getDisplayName());
                }

                PlayerListPlugin.setPlayerList(newList);
                DiscordMain.updateBot();
                return true;
            }
            default: return false;
        }
    }

    private boolean telegramCommand(String[] args, CommandSender sender)
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
                            sender.sendMessage("Telegram token is " + Config.getTelegramToken());
                            break;
                        }
                        case 3:
                        {
                            sender.sendMessage("Usage: /playerlist telegram token set <token>");
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
                    sender.sendMessage("Usage: /playerlist telegram <disconnect|reconnect|token>");
                    break;
                }
            }
            return true;
        }
        else return false;
    }

    private boolean discordCommand(String[] args, CommandSender sender)
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
                            sender.sendMessage("Discord token is " + Config.getDiscordToken());
                            break;
                        }
                        case 3:
                        {
                            sender.sendMessage("Usage: /playerlist discord token set <token>");
                            break;
                        }
                        case 4:
                        {
                            if(args[2].equalsIgnoreCase("set"))
                            {
                                sender.sendMessage("Setting discord token to: " + args[3]);
                                Config.setDiscordToken(args[4]);
                            }
                            else sender.sendMessage("Usage: /playerlist discord token set <token>");
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    sender.sendMessage("Usage: /playerlist discord <disconnect|reconnect|token>");
                    break;
                }
            }
            return true;
        }
        else return false;
    }
}
