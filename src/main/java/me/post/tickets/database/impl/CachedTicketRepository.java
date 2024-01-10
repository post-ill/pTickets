package me.post.tickets.database.impl;

import me.post.tickets.database.TicketRepository;
import me.post.tickets.database.model.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Predicate;

public class CachedTicketRepository implements TicketRepository {
    private final @NotNull Collection<Ticket> tickets = new LinkedList<>();

    @Override
    public void addTicket(@NotNull Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public void removeTicket(@NotNull Ticket ticket) {
        tickets.remove(ticket);
    }

    @Override
    public @NotNull Collection<Ticket> listTickets() {
        return tickets;
    }

    @Override
    public @Nullable Ticket findTicket(@NotNull Predicate<Ticket> criteria) {
        return tickets.stream()
            .filter(criteria)
            .findFirst()
            .orElse(null);
    }
}
