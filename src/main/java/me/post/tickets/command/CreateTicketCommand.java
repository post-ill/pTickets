package me.post.tickets.command;

import me.post.lib.command.CommandModule;
import me.post.lib.command.annotation.Command;
import me.post.tickets.database.TicketRepository;
import me.post.tickets.database.model.Ticket;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.post.lib.util.Pairs.to;
import static me.post.tickets.Response.respond;

public class CreateTicketCommand implements CommandModule {
    private final @NotNull TicketRepository ticketRepository;

    public CreateTicketCommand(@NotNull TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Command(name = "ticket")
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            respond(sender, "Comando-jogador");
            return;
        }

        final Player player = (Player) sender;

        if (args.length < 2) {
            respond(sender, "Criar-ticket-ajuda");
            return;
        }

        if (ticketRepository.findTicket(ticket -> ticket.author().equals(player.getName())) != null) {
            respond(sender, "Criar-ticket-existente");
            return;
        }

        final String subject = args[0];
        final String content = args[1];
        final Ticket ticket = new Ticket(player.getName(), subject, content, System.currentTimeMillis());
        ticketRepository.addTicket(ticket);
        respond(sender, "Ticket-criado", message -> message.replace(
            to("@assunto", ticket.subject())
        ));
    }
}
