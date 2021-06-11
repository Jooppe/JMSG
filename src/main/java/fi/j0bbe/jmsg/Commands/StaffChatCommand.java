package fi.j0bbe.jmsg.Commands;

import fi.j0bbe.jmsg.Listeners.StaffChatListener;
import fi.j0bbe.jmsg.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand extends Command {

    public static List<ProxiedPlayer> staffChat = new ArrayList<>();

    public StaffChatCommand() {
        super("staffchat", "jmsg.commands.staffchat", "sc", "ypchat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        String prefix = Main.getConfig().getString("messages.prefix");

        if (staffChat.contains((ProxiedPlayer) sender)) {
            sender.sendMessage(prefix + Main.getConfig().getString("messages.staffchat-disabled"));
            staffChat.remove((ProxiedPlayer) sender);
        } else {
            sender.sendMessage(prefix + Main.getConfig().getString("messages.staffchat-enabled"));
            StaffChatListener.see.add((ProxiedPlayer) sender);
            staffChat.add((ProxiedPlayer) sender);
        }
    }
}
