package com.gmail.leeyi45.PlayerListPlugin.discordBot;

import com.gmail.leeyi45.PlayerListPlugin.pluginMain.Config;
import com.gmail.leeyi45.PlayerListPlugin.pluginMain.PlayerListPlugin;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.logging.Level;

public class MessageListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.isFromType(ChannelType.TEXT))
        {
            Message msg = event.getMessage();
            String msgText = msg.getContentStripped();

            if(msgText.startsWith(Config.discordCommandPrefix()))
            { //Message is actually a command

                //We remove the command prefix
                String[] args = msgText.substring(1).split(" ");

                //And then we send everything to the processor
                String reply = CommandProcessor.processCommand(msg, args);

                //And reply to the channel that sent it
                event.getTextChannel().sendMessage(reply).queue();
                PlayerListPlugin.logToConsole(String.format("Command '%s' received from user '%s' from channel '%s'",
                        args[0], event.getMember().getEffectiveName(), event.getChannel().getName()), Level.INFO);
            }
        }

    }
}
