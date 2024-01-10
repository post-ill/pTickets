package me.post.tickets.command;

import me.post.lib.command.CommandModule;
import me.post.lib.command.annotation.Command;
import me.post.lib.common.Updatable;
import me.post.lib.config.wrapper.ConfigManager;
import me.post.tickets.Response;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static me.post.tickets.Response.respond;

public class ReloadCommand implements CommandModule {
    private final @NotNull ConfigManager configManager;
    private final @NotNull Updatable[] dependants;

    public ReloadCommand(@NotNull ConfigManager configManager, @NotNull Updatable... dependants) {
        this.configManager = configManager;
        this.dependants = dependants;
    }

    @Command(name = "ticket reload")
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!sender.hasPermission("pticket.reload")) {
            Response.respond(sender, "Comando-permissoes");
            return;
        }

        configManager.reloadAll();
        Arrays.asList(dependants).forEach(Updatable::update);
        Response.respond(sender, "Reload");
    }
}
