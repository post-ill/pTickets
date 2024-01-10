package me.post.tickets.database.model;

import org.jetbrains.annotations.NotNull;

public class Ticket {
    private final @NotNull String author;
    private final @NotNull String subject;
    private final @NotNull String content;
    private final long timestamp;

    public Ticket(@NotNull String author, @NotNull String subject, @NotNull String content, long timestamp) {
        this.author = author;
        this.subject = subject;
        this.content = content;
        this.timestamp = timestamp;
    }

    public @NotNull String author() {
        return author;
    }

    public @NotNull String subject() {
        return subject;
    }

    public @NotNull String content() {
        return content;
    }

    public long timestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Ticket)) {
            return false;
        }

        return ((Ticket) other).author.equals(this.author);
    }
}
