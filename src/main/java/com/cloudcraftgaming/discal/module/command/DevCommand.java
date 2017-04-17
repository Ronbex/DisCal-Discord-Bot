package com.cloudcraftgaming.discal.module.command;

import com.cloudcraftgaming.discal.Main;
import com.cloudcraftgaming.discal.database.DatabaseManager;
import com.cloudcraftgaming.discal.internal.data.GuildSettings;
import com.cloudcraftgaming.discal.module.command.info.CommandInfo;
import com.cloudcraftgaming.discal.utils.Message;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.ArrayList;

/**
 * Created by Nova Fox on 4/4/2017.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCal
 */
public class DevCommand implements ICommand {

    /**
     * Gets the command this Object is responsible for.
     *
     * @return The command this Object is responsible for.
     */
    @Override
    public String getCommand() {
        return "dev";
    }

    /**
     * Gets the short aliases of the command this object is responsible for.
     * </br>
     * This will return an empty ArrayList if none are present
     *
     * @return The aliases of the command.
     */
    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    /**
     * Gets the info on the command (not sub command) to be used in help menus.
     *
     * @return The command info.
     */
    @Override
    public CommandInfo getCommandInfo() {
        CommandInfo ci = new CommandInfo("dev");
        ci.setDescription("Used for developer commands. Only able to be used by registered developers");
        ci.setExample("!dev <function> (value)");
        ci.getSubCommands().add("patron");
        ci.getSubCommands().add("dev");
        ci.getSubCommands().add("maxcal");

        return ci;
    }

    /**
     * Issues the command this Object is responsible for.
     *
     * @param args   The command arguments.
     * @param event  The event received.
     * @return <code>true</code> if successful, else <code>false</code>.
     */
    @Override
    public Boolean issueCommand(String[] args, MessageReceivedEvent event) {
        String novaId = "130510525770629121";
        String xaanitId = "233611560545812480";
        if (event.getMessage().getAuthor().getID().equals(novaId) || event.getMessage().getAuthor().getID().equals(xaanitId)) {
            if (args.length < 1) {
                Message.sendMessage("Please specify the function you would like to execute. To view valid functions use `!help dev`", event);
            } else if (args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "patron":
                        modulePatron(args, event);
                        break;
                    case "dev":
                        moduleDevGuild(args, event);
                        break;
                    case "maxcal":
                        moduleMaxCalendars(args, event);
                        break;
                    default:
                        Message.sendMessage("Invalid sub command! Use `!help dev` to view valid sub commands!", event);
                        break;
                }
            }
        } else {
            Message.sendMessage("You are not a registered DisCal developer! If this is a mistake please contact Nova!", event);
        }
        return false;
    }

    private void modulePatron(String[] args, MessageReceivedEvent event) {
        if (args.length == 2) {
            String guildId = args[1];
            if (Main.client.getGuildByID(guildId) != null) {
                GuildSettings settings = DatabaseManager.getManager().getSettings(guildId);
                settings.setPatronGuild(!settings.isPatronGuild());

                Boolean isPatron = settings.isPatronGuild();

                DatabaseManager.getManager().updateSettings(settings);

                Message.sendMessage("Guild with ID: `" + guildId + "` is patron set to: `" + isPatron + "`", event);
            } else {
                Message.sendMessage("Guild not found or is not connected to DisCal!", event);
            }
        } else {
            Message.sendMessage("Please specify the ID of the guild to set as a patron guild with `!dev patron <ID>`", event);
        }
    }

    private void moduleDevGuild(String[] args, MessageReceivedEvent event) {
        if (args.length == 2) {
            String guildId = args[1];
            if (Main.client.getGuildByID(guildId) != null) {
                GuildSettings settings = DatabaseManager.getManager().getSettings(guildId);
                settings.setDevGuild(!settings.isDevGuild());

                Boolean isPatron = settings.isDevGuild();

                DatabaseManager.getManager().updateSettings(settings);

                Message.sendMessage("Guild with ID: `" + guildId + "` is dev guild set to: `" + isPatron + "`", event);
            } else {
                Message.sendMessage("Guild not found or is not connected to DisCal!", event);
            }
        } else {
            Message.sendMessage("Please specify the ID of the guild to set as a dev guild with `!dev dev <ID>`", event);
        }
    }

    private void moduleMaxCalendars(String[] args, MessageReceivedEvent event) {
        if (args.length == 3) {
            String guildId = args[1];
            try {
                Integer mc = Integer.valueOf(args[2]);
                mc = Math.abs(mc);
                if (Main.client.getGuildByID(guildId) != null) {
                    GuildSettings settings = DatabaseManager.getManager().getSettings(guildId);
                    settings.setMaxCalendars(mc);

                    DatabaseManager.getManager().updateSettings(settings);

                    Message.sendMessage("Guild with ID: `" + guildId + "` max calendar count set to: `" + mc + "`", event);
                } else {
                    Message.sendMessage("Guild not found or is not connected to DisCal!", event);
                }
            } catch (NumberFormatException e) {
                Message.sendMessage("Max Calendar amount must be a valid Integer!", event);
            }
        } else {
            Message.sendMessage("Please specify the ID of the guild and calendar amount with `!dev maxcal <ID> <amount>`", event);
        }
    }
}