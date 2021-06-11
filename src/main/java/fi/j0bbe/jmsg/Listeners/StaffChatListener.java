package fi.j0bbe.jmsg.Listeners;

import fi.j0bbe.jmsg.Commands.StaffChatCommand;
import fi.j0bbe.jmsg.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;

public class StaffChatListener implements Listener {
    public static ArrayList<ProxiedPlayer> see = new ArrayList<ProxiedPlayer>();

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (player.hasPermission("jmsg.commands.staffchat")) {
            if (!event.getMessage().startsWith("/")) {
                if (event.getMessage().startsWith("# ")) {
                    String msg = event.getMessage().replace("# ", "");
                    see.add(player);
                    for (ProxiedPlayer p : Main.getInstance().getProxy().getPlayers()) {
                        if (p.hasPermission("jmsg.commands.staffchat") || StaffChatCommand.staffChat.contains(p)) {
                            p.sendMessage(Main.getConfig().getString("messages.staffchat-format").replace("{sender}", player.getName()).replace("{message}", msg));
                        }
                    }
                    event.setCancelled(true);
                } else if (StaffChatCommand.staffChat.contains(player)) {
                    for (ProxiedPlayer p : Main.getInstance().getProxy().getPlayers()) {
                        if (p.hasPermission("jmsg.commands.staffchat") || StaffChatCommand.staffChat.contains(p)) {

                            p.sendMessage(Main.getConfig().getString("messages.staffchat-format").replace("{sender}", player.getName()).replace("{message}", event.getMessage()));
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
