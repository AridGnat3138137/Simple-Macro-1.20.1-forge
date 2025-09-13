package xyz.hajsori.simplemacro.config;

import de.maxhenkel.voicechat.configbuilder.ConfigBuilder;
import de.maxhenkel.voicechat.configbuilder.entry.ConfigEntry;
import xyz.hajsori.simplemacro.Constants;

public class ClientConfig {
    public ConfigEntry<Integer> port;
    public ConfigEntry<Boolean> logMessages;

    public ClientConfig(ConfigBuilder builder) {
        builder.header(String.format("%s client config %s", Constants.MOD_NAME, Constants.MOD_VERSION));

        port = builder
                .integerEntry("port", 0, 0, 65535,
                        "The port to open the web socket server on",
                        "0 = random port"
                );
        logMessages = builder
                .booleanEntry("log_messages", false,
                        "Log sent and received messages"
                );
    }
}
