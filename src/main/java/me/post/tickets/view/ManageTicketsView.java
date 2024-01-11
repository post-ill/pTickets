package me.post.tickets.view;

import me.post.configlib.config.common.ItemDisplay;
import me.post.configlib.config.model.MenuModel;
import me.post.configlib.util.ItemTransformer;
import me.post.configlib.view.PageConfigContextBuilder;
import me.post.lib.util.Heads;
import me.post.lib.util.Time;
import me.post.lib.view.Views;
import me.post.lib.view.impl.ContextView;
import me.post.tickets.database.TicketRepository;
import me.post.tickets.database.model.Ticket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.post.lib.util.Pairs.to;
import static me.post.tickets.Response.respond;

public class ManageTicketsView extends ContextView {
    private final @NotNull MenuModel menuModel;
    private final @NotNull TicketRepository ticketRepository;

    public ManageTicketsView(@NotNull MenuModel menuModel, @NotNull TicketRepository ticketRepository) {
        this.menuModel = menuModel;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void open(@NotNull Player player, @Nullable Object data) {
        final List<Integer> slots = menuModel.data().getData("Slots");
        final ItemDisplay displayTicket = menuModel.data().getData("Display-ticket");
        final String contentLineFormat = menuModel.data().getData("Formato-linha-conteudo");

        PageConfigContextBuilder
            .of(
                menuModel,
                page -> Views.get().open(player, ManageTicketsView.class, page),
                data == null ? 0 : (int) data
            )
            .editTitle((pageInfo, title) ->
                title
                    .replace("@atual", Integer.toString(pageInfo.page() + 1))
                    .replace("@totais", Integer.toString(pageInfo.totalPages()))
            )
            .withSlots(slots)
            .nextPageButtonAs("Pagina-seguinte")
            .previousPageButtonAs("Pagina-anterior")
            .fill(
                ticketRepository.listTickets(),
                ticket -> ItemTransformer.of(Heads.fromOwner(ticket.author(), null))
                    .display(displayTicket)
                    .nameFormat(to("@autor", ticket.author()))
                    .loreFormat(
                        to("@assunto", ticket.subject()),
                        to("@tempo", Time.toFormat((int) TimeUnit.MILLISECONDS.toSeconds(
                            System.currentTimeMillis() - ticket.timestamp()
                        )))
                    )
                    .loreListFormat("@conteudo", prettifyContent(ticket.content(), 40, contentLineFormat))
                    .colorMeta()
                    .transform(),
                (ticket, click) -> {
                    final Ticket currentTicket = ticketRepository.findTicket(current ->
                        current.author().equals(ticket.author())
                    );

                    if (currentTicket == null) {
                        Views.get().open(player, ManageTicketsView.class);
                        respond(player, "Ticket-inexistente");
                        return;
                    }

                    if (click.clickType() == ClickType.LEFT) {
                        final Player authorPlayer = Bukkit.getPlayer(currentTicket.author());

                        if (authorPlayer == null) {
                            respond(player, "Jogador-offline", message -> message.replace(
                                to("@jogador", currentTicket.author())
                            ));
                            return;
                        }

                        player.teleport(authorPlayer.getLocation());
                        return;
                    }

                    if (click.clickType() == ClickType.RIGHT) {
                        ticketRepository.removeTicket(ticket);
                        Views.get().open(player, ManageTicketsView.class);
                        respond(player, "Ticket-removido", message -> message.replace(
                            to("@jogador", currentTicket.author())
                        ));
                    }
                }
            )
            .build(viewContext, player, null);
    }

    private @NotNull List<String> prettifyContent(@NotNull String content, int lineSize, @NotNull String lineFormat) {
        final List<String> contentLines = new ArrayList<>(
            (int) Math.ceil((double) content.length() / lineSize)
        );

        for (int i = 0; i < content.length(); i += lineSize) {
            contentLines.add(lineFormat.replace(
                "@linha",
                content.substring(i, Math.min(i + lineSize, content.length()))
            ));
        }

        return contentLines;
    }
}
