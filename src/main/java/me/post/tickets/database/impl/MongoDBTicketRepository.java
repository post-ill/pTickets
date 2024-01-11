package me.post.tickets.database.impl;

import com.mongodb.client.MongoDatabase;
import me.post.tickets.database.TicketRepository;
import me.post.tickets.database.model.Ticket;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBTicketRepository implements TicketRepository {
    private final @NotNull MongoDatabase mongoDb;
    private final @NotNull Collection<Ticket> tickets = new LinkedList<>();

    private final @NotNull String TICKETS_COLLECTION = "ptickets_tickets";

    public MongoDBTicketRepository(@NotNull MongoDatabase mongoDb) {
        this.mongoDb = mongoDb;
        cacheTickets();
    }

    private void cacheTickets() {
        mongoDb.getCollection(TICKETS_COLLECTION).find().forEach(document ->
            tickets.add(new Ticket(
                document.getString("author"),
                document.getString("subject"),
                document.getString("content"),
                document.getLong("timestamp")
            ))
        );
    }

    @Override
    public void addTicket(@NotNull Ticket ticket) {
        CompletableFuture.runAsync(() ->
            mongoDb.getCollection(TICKETS_COLLECTION).insertOne(new Document()
                .append("_id", new ObjectId())
                .append("author", ticket.author())
                .append("subject", ticket.subject())
                .append("content", ticket.content())
                .append("timestamp", ticket.timestamp())
            )
        );
        tickets.add(ticket);
    }

    @Override
    public void removeTicket(@NotNull Ticket ticket) {
        CompletableFuture.runAsync(() ->
            mongoDb.getCollection(TICKETS_COLLECTION).deleteOne(eq("author", ticket.author()))
        );
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
