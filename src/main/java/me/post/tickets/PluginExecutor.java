package me.post.tickets;

import me.post.tickets.command.*;
import me.post.configlib.config.Updatables;
import me.post.configlib.config.model.MenuModel;
import me.post.configlib.config.model.impl.FullReadMenuModel;
import me.post.lib.config.wrapper.ConfigManager;
import me.post.lib.config.wrapper.YamlConfigManager;
import me.post.tickets.config.MainConfig;
import me.post.tickets.database.TicketRepository;
import me.post.tickets.database.impl.CachedTicketRepository;
import me.post.tickets.database.model.Ticket;
import me.post.tickets.ticket.TicketCreationDelay;
import me.post.tickets.view.ManageTicketsView;
import org.jetbrains.annotations.NotNull;
import me.post.lib.command.process.CommandRegistry;
import me.post.lib.view.Views;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class PluginExecutor {
    private final @NotNull JavaPlugin plugin;
    private final @NotNull ConfigManager configManager;
    private final @NotNull Updatables updatables;

    private final @NotNull MainConfig mainConfig;

    private final @NotNull TicketRepository ticketRepository;
    private final @NotNull TicketCreationDelay ticketCreationDelay;

    public PluginExecutor(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        configManager = new YamlConfigManager(plugin);
        loadConfiguration();

        updatables = new Updatables();
        mainConfig = updatables.include(new MainConfig(configManager.getWrapper("config")));

        ticketRepository = new CachedTicketRepository();
        ticketCreationDelay = new TicketCreationDelay(mainConfig);
    }

    private void loadConfiguration() {
        Arrays
            .asList(
                "config",
                "resposta/mensagens", "resposta/sons", "resposta/efeitos",
                "menu/gerenciar_tickets"
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
            new ManageTicketsCommand(),
            new CreateTicketCommand(ticketRepository, ticketCreationDelay),
            new TicketInfoCommand(ticketRepository),
            new CloseTicketCommand(ticketRepository),
            new ReloadCommand(configManager, updatables),
            new HelpCommand()
        );

        registry.registerAll();
    }

    private void registerViews() {
        Views.get().register(
            new ManageTicketsView(menuModel("menu/gerenciar_tickets"), ticketRepository)
        );
    }

    private @NotNull MenuModel menuModel(@NotNull String configPath) {
        return updatables.include(new FullReadMenuModel(configManager.getWrapper(configPath)));
    }
}
