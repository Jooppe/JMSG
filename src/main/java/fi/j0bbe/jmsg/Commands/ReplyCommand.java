package fi.j0bbe.jmsg.Commands;

import fi.j0bbe.jmsg.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCommand extends Command {

    public ReplyCommand() {
        super("r", "jmsg.commands.reply", "reply", "answer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        String prefix = Main.getConfig().getString("messages.prefix");

        if (args.length == 0) {
            sender.sendMessage(prefix + Main.getConfig().getString("messages.reply-help"));
        } else if (!(sender instanceof ProxiedPlayer)) {
            if (!MsgCommand.messagers.containsKey("proxy")) {
                sender.sendMessage(prefix + Main.getConfig().getString("messages.nobody-to-reply"));
                return;
            }

            ProxiedPlayer target = Main.getInstance().getProxy().getPlayer(MsgCommand.messagers.get("proxy"));

            if (target == null) {

                sender.sendMessage(prefix + Main.getConfig().getString("messages.target-offline"));
                return;

            }

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                str.append(args[i] + " ");
            }
            String nmessage = str.toString();
            //String message = nmessage.replace("&", "§");

            ProxiedPlayer senderd = (ProxiedPlayer) sender;

            sender.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", senderd.getDisplayName()).replace("{receiver}", target.getName()).replace("{message}", nmessage));
            target.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", target.getName()).replace("{message}", nmessage));

            MsgCommand.messagers.put(target.getName(), "proxy");
            MsgCommand.messagers.put("proxy", target.getName());

        } else {
            if (!MsgCommand.messagers.containsKey(sender.getName())) {
                sender.sendMessage(prefix + Main.getConfig().getString("messages.nobody-to-reply"));
                return;
            }

            if (MsgCommand.messagers.get(sender.getName()).equalsIgnoreCase("proxy")) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    str.append(args[i] + " ");
                }
                String nmessage = str.toString();
                String message = nmessage.replace("&", "§");

                sender.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", "CONSOLE").replace("{message}", nmessage));
                Main.getInstance().getProxy().getConsole().sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", "CONSOLE").replace("{message}", message));

                MsgCommand.messagers.put("proxy", sender.getName());
                MsgCommand.messagers.put(sender.getName(), "proxy");
                return;
            }

            ProxiedPlayer target = Main.getInstance().getProxy().getPlayer(MsgCommand.messagers.get(sender.getName()));
            if (target == null) {
                sender.sendMessage(prefix + Main.getConfig().getString("messages.target-offline"));

            } else {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    str.append(args[i] + " ");
                }
                String nmessage = str.toString();

                sender.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", target.getName()).replace("{message}", nmessage));
                target.sendMessage(Main.getConfig().getString("messages.msg-format").replace("{sender}", sender.getName()).replace("{receiver}", target.getName()).replace("{message}", nmessage));

                MsgCommand.messagers.put(target.getName(), sender.getName());
                MsgCommand.messagers.put(sender.getName(), target.getName());
            }

        }
    }
}
