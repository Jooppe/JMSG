package fi.j0bbe.jmsg;

import fi.j0bbe.jmsg.Commands.MsgCommand;
import fi.j0bbe.jmsg.Commands.ReplyCommand;
import fi.j0bbe.jmsg.Commands.StaffChatCommand;
import fi.j0bbe.jmsg.Listeners.StaffChatListener;
import fi.j0bbe.jmsg.Utils.UpdateChecker;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class Main extends Plugin {

    public static Main instance;
    public static Main getInstance() { return instance; }
    public Main() { instance = this; }

    private static Configuration config;

    @Override
    public void onEnable() {
        System.out.println("[JMsg] Enabling...");

        instance = this;

        new UpdateChecker(this, 93182).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                System.out.println("[JMsg] There is not a new update available.");
            } else {
                System.out.println("[JMsg] There is a new update available.");
            }
        });

        saveDefaultConfig();

        try {
            loadConfig();
        } catch (IOException ex) {
            System.err.println("COULD NOT LOAD CONFIG.YML! MAKE SURE THE FILE EXISTS!");
            ex.printStackTrace();
            return;
        }

        getProxy().getPluginManager().registerCommand(this, new MsgCommand());
        getProxy().getPluginManager().registerCommand(this, new ReplyCommand());
        getProxy().getPluginManager().registerCommand(this, new StaffChatCommand());
        getProxy().getPluginManager().registerListener(this, new StaffChatListener());

    }

    @Override
    public void onDisable() {
        System.out.println("[JMsg] Disabling...");
    }

    public void saveConfig() throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
    }

    public void loadConfig() throws IOException {
        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
    }

    public static Configuration getConfig() {
        return config;
    }

    private void saveDefaultConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) try {
            InputStream in = getResourceAsStream("config.yml");
            Throwable localThrowable3 = null;
            try {
                Files.copy(in, file.toPath());
            } catch (Throwable localThrowable1) {
                localThrowable3 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (in != null) if (localThrowable3 != null) try {
                    in.close();
                } catch (Throwable localThrowable2) {
                    localThrowable3.addSuppressed(localThrowable2);
                }
                else in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
