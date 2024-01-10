package me.post.tickets.command;

import me.post.lib.command.CommandModule;
import me.post.lib.command.annotation.Command;
import me.post.lib.util.Time;
import me.post.tickets.database.TicketRepository;
import me.post.tickets.database.model.Ticket;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static me.post.lib.util.Pairs.to;
import static me.post.tickets.Response.respond;

public class TicketInfoCommand implements CommandModule {
    private final @NotNull TicketRepository ticketRepository;

    public TicketInfoCommand(@NotNull TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Command(name = "ticket info")
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            respond(sender, "Comando-jogador");
            return;
        }

        final Player player = (Player) sender;
        final Ticket ticket = ticketRepository.findTicket(current -> current.author().equals(player.getName()));

        if (ticket == null) {
            respond(sender, "Ticket-info-inexistente");
            return;
        }

        respond(sender, "Ticket-info", message -> message
            .replace(to("@assunto", ticket.subject()))
            .replace(to("@conteudo", ticket.content()))
            .replace(to("@tempo", Time.toFormat((int) TimeUnit.MILLISECONDS.toSeconds(
                System.currentTimeMillis() - ticket.timestamp()
            ))))
        );
    }
}
