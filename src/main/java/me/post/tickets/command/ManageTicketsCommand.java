package me.post.tickets.command;

import me.post.lib.command.CommandModule;
import me.post.lib.command.annotation.Command;
import me.post.lib.view.Views;
import me.post.tickets.view.ManageTicketsView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.post.tickets.Response.respond;

public class ManageTicketsCommand implements CommandModule {
    @Command(name = "tickets")
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            respond(sender, "Comando-jogador");
            return;
        }

        if (!sender.hasPermission("ptickets.manage_tickets")) {
            respond(sender, "Comando-permissoes");
            return;
        }

        final Player player = (Player) sender;
        Views.get().open(player, ManageTicketsView.class);
    }
}
