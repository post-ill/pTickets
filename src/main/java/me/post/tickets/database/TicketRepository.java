package me.post.tickets.database;

import me.post.tickets.database.model.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public interface TicketRepository {
    void addTicket(@NotNull Ticket ticket);

    void removerTicket(@NotNull Ticket ticket);

    @NotNull Collection<Ticket> listTickets();

    @Nullable Ticket findTicket(@NotNull Predicate<Ticket> criteria);
}
