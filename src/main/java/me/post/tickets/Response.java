package me.post.tickets;

import me.post.configlib.config.Updatables;
import me.post.configlib.config.model.impl.EffectsModel;
import me.post.configlib.config.model.impl.MessagesModel;
import me.post.configlib.config.model.impl.SoundsModel;
import me.post.configlib.config.model.impl.data.EffectsData;
import me.post.configlib.config.model.impl.data.MessagesData;
import me.post.configlib.config.model.impl.data.SoundsData;
import me.post.configlib.config.model.impl.response.effect.PlayerEffect;
import me.post.configlib.config.model.impl.response.message.SubjectMessage;
import me.post.configlib.config.model.impl.response.sound.PlayerSound;
import me.post.lib.config.model.ConfigModel;
import me.post.lib.config.wrapper.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Response {
    private static ConfigModel<MessagesData> messages;
    private static ConfigModel<SoundsData> sounds;
    private static ConfigModel<EffectsData> effects;

    public static void setup(@NotNull Updatables updatables, @NotNull ConfigManager configManager) {
        messages = updatables.include(new MessagesModel(configManager.getWrapper("resposta/mensagens")));
        sounds = updatables.include(new SoundsModel(configManager.getWrapper("resposta/sons")));
        effects = updatables.include(new EffectsModel(configManager.getWrapper("resposta/efeitos")));
    }

    public static <T extends CommandSender> void respond(
        @Nullable T sender,
        @NotNull String key,
        @NotNull Function<SubjectMessage, SubjectMessage> transformFunction
    ) {
        message(sender, key, transformFunction);

        if (!(sender instanceof Player)) {
            return;
        }

        final PlayerEffect effect = effects.data().getEffect(key);
        final PlayerSound sound = sounds.data().getSound(key);

        if (effect != null) {
            effect.send(sender);
        }

        if (sound != null) {
            sound.send(sender);
        }
    }

    public static <T extends CommandSender> void respond(@Nullable T sender, @NotNull String key) {
        respond(sender, key, Function.identity());
    }

    private static void message(
        @Nullable CommandSender sender,
        @NotNull String key,
        @NotNull Function<SubjectMessage, SubjectMessage> transformFunction
    ) {
        final SubjectMessage message = messages.data().getMessage(key);

        if (message == null) {
            return;
        }

        transformFunction.apply(message).color().send(sender);
    }

    private static void message(@Nullable CommandSender sender, @NotNull String key) {
        message(sender, key, Function.identity());
    }
}
