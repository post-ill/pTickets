package me.post.tickets.command;

import me.post.lib.command.CommandModule;
import me.post.lib.command.annotation.Command;
import me.post.tickets.database.TicketRepository;
import me.post.tickets.database.model.Ticket;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.post.tickets.Response.respond;

public class CloseTicketCommand implements CommandModule {
    private final @NotNull TicketRepository ticketRepository;

    public CloseTicketCommand(@NotNull TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Command(name = "ticket fechar")
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            respond(sender, "Comando-jogador");
            return;
        }

        final Player player = (Player) sender;
        final Ticket ticket = ticketRepository.findTicket(current -> current.author().equals(player.getName()));

        if (ticket == null) {
            respond(sender, "Ticket-fechar-inexistente");
            return;
        }

        ticketRepository.removeTicket(ticket);
        respond(sender, "Ticket-fechado");
    }
}
