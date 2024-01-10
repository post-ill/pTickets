package me.post.tickets.command;

import me.post.lib.command.CommandModule;
import me.post.lib.command.annotation.Command;
import me.post.tickets.Response;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.post.tickets.Response.respond;

public class HelpCommand implements CommandModule {
    @Command(name = "ticket ajuda")
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        Response.respond(sender, sender.hasPermission("ptickets.ajuda_staff") ? "Ajuda-staff" : "Ajuda");
    }
}
