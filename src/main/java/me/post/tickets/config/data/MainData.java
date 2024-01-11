package me.post.tickets.config.data;

public class MainData {
    private final int ticketCreationDelay;

    public MainData(int ticketCreationDelay) {
        this.ticketCreationDelay = ticketCreationDelay;
    }

    public int ticketCreationDelay() {
        return ticketCreationDelay;
    }
}
