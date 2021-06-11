package fi.j0bbe.jmsg.Commands;

import fi.j0bbe.jmsg.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.Map;

public class MsgCommand extends Command {

    public static Map<String, String> messagers = new HashMap<>();

    public MsgCommand() {
        super("msg", "jmsg.commands.msg", "message", "tell", "whisper", "w", "pm", "epm", "say");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        String prefix = Main.getConfig().getString("messages.prefix");

        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(prefix + Main.getConfig().getString("messages.msg-help"));
        } else {
            if (!(sender instanceof ProxiedPlayer)) {
                if (args[0].equalsIgnoreCase("proxy")) {
                    sender.sendMessage(prefix + Main.getConfig().getString("trying-msg-itself"));
                } else {
                    StringBuilder sb = new StringBuilder();

                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i] + " ");
                    }

                    String message = sb.toString();

                    ProxiedPlayer target = Main.getInstance().getProxy().getPlayer(args[0]);

                    if (target != null) {

                        sender.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", target.getName()).replace("{message}", message));
                        target.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", target.getName()).replace("{message}", message));

                        messagers.put(target.getName(), "proxy");
                        messagers.put("proxy", target.getName());

                    } else {
                        sender.sendMessage(prefix + Main.getConfig().getString("messages.target-offline"));
                    }
                }
            } else {

                if (args[0].equalsIgnoreCase(sender.getName())) {
                    sender.sendMessage(prefix + Main.getConfig().getString("trying-msg-itself"));

                } else {

                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i] + " ");
                    }

                    String message = sb.toString();

                    ProxiedPlayer target = Main.getInstance().getProxy().getPlayer(args[0]);

                    try {
                        if (!target.isConnected()) {
                            sender.sendMessage(prefix + Main.getConfig().getString("messages.target-offline"));
                        }
                    } catch (NullPointerException e) {
                        sender.sendMessage(prefix + Main.getConfig().getString("messages.target-offline"));
                    }

                    if (target != null) {

                        sender.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", target.getName()).replace("{message}", message));
                        target.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", target.getName()).replace("{message}", message));

                        messagers.put(target.getName(), sender.getName());
                        messagers.put(sender.getName(), target.getName());

                    }
                }
            }
        }
    }
}
