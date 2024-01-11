package me.post.tickets.config;

import me.post.lib.config.model.impl.ConfigModelWrapper;
import me.post.lib.config.wrapper.ConfigWrapper;
import me.post.tickets.config.data.MainData;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class MainConfig extends ConfigModelWrapper<MainData> {
    public MainConfig(@NotNull ConfigWrapper configWrapper) {
        super(configWrapper);
    }

    @NotNull
    @Override
    public MainData get(@NotNull FileConfiguration config) {
        return new MainData(
            config.getInt("Criar-ticket-delay")
        );
    }
}
