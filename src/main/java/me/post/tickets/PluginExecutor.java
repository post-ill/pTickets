package me.post.tickets;

import me.post.tickets.command.HelpCommand;
import me.post.tickets.command.ReloadCommand;
import me.post.configlib.config.Updatables;
import me.post.configlib.config.model.MenuModel;
import me.post.configlib.config.model.impl.FullReadMenuModel;
import me.post.lib.config.wrapper.ConfigManager;
import me.post.lib.config.wrapper.YamlConfigManager;
import me.post.lib.util.Scheduler;
import org.jetbrains.annotations.NotNull;
import me.post.lib.command.process.CommandRegistry;
import me.post.lib.view.Views;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class PluginExecutor {
    private final @NotNull JavaPlugin plugin;
    private final @NotNull ConfigManager configManager;
    private final @NotNull Updatables updatables;

    public PluginExecutor(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        configManager = new YamlConfigManager(plugin);
        loadConfiguration();

        updatables = new Updatables();
    }

    private void loadConfiguration() {
        Arrays
            .asList(
                "config",
                "resposta/mensagens", "resposta/sons", "resposta/efeitos"
            )
            .forEach(configManager::load);
    }

    /**
     * @return A runnable executed when disabling.
     * */
    public @NotNull Runnable execute() {
        Response.setup(updatables, configManager);

        registerCommands();
        registerViews();

        updatables.update();
        return () -> {};
    }

    private void registerCommands() {
        final CommandRegistry registry = CommandRegistry.on(plugin);

        registry.addModules(
            new ReloadCommand(configManager, updatables),
            new HelpCommand()
        );

        registry.registerAll();
    }

    private void registerViews() {
        Views.get().register(

        );
    }

    private @NotNull MenuModel menuModel(@NotNull String configPath) {
        return updatables.include(new FullReadMenuModel(configManager.getWrapper(configPath)));
    }
}
