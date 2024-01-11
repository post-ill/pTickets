package me.post.tickets.ticket;

import me.post.tickets.config.MainConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TicketCreationDelay {
    private final @NotNull MainConfig mainConfig;
    private final @NotNull Map<UUID, Long> players = new HashMap<>();

    public TicketCreationDelay(@NotNull MainConfig mainConfig) {
        this.mainConfig = mainConfig;
    }

    public void setDelay(@NotNull Player player) {
        players.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public int delay(@NotNull Player player) {
        final Long delay = players.get(player.getUniqueId());

        if (delay == null) {
            return 0;
        }

        final int elapsedSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - delay);

        if (elapsedSeconds >= mainConfig.data().ticketCreationDelay()) {
            players.remove(player.getUniqueId());
            return 0;
        }

        return mainConfig.data().ticketCreationDelay() - elapsedSeconds;
    }
}
