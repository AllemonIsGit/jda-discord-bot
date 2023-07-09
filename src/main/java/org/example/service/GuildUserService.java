package org.example.service;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.example.domain.model.GuildUser;
import org.example.repository.GuildUserRepository;
import org.example.util.MessageCache;

public class GuildUserService {

    private static GuildUserService INSTANCE;
    private final GuildUserRepository guildUserRepository;
    private final MessageCache messageCache;

    private GuildUserService() {
        this.guildUserRepository = GuildUserRepository.getInstance();
        this.messageCache = MessageCache.getInstance();
    }

    public static GuildUserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GuildUserService();
            return INSTANCE;
        }
        return INSTANCE;
    }

    public void revokePoints(MessageReactionRemoveEvent event) {
        User user = event.getUser();
        String snowflakeId = user.getId();
        String discordTag = user.getAsTag();

        if (!guildUserRepository.existsBySnowflakeId(user.getId())) {
            create(snowflakeId, discordTag);
        }
        GuildUser guildUser = guildUserRepository.getGuildUserBySnowflakeId(snowflakeId);
        removePoints(guildUser, 1L);
    }

    public void revokePoints(MessageDeleteEvent event) {
        String messageId = event.getMessageId();
        if (!messageCache.existsByMessageId(messageId)) {
            return;
        }
        String authorSnowflakeId = messageCache.getAuthorIdByMessageId(messageId);
        removePoints(authorSnowflakeId, 1L);
    }

    public void revokePoints(String snowflakeId, Long amount) {
        removePoints(snowflakeId, amount);
    }

    public void awardPoints(MessageReactionAddEvent event) {
        User user = event.getUser();
        String snowflakeId = user.getId();
        String discordTag = user.getAsTag();

        if (!guildUserRepository.existsBySnowflakeId(user.getId())) {
            create(snowflakeId, discordTag);
        }
        GuildUser guildUser = guildUserRepository.getGuildUserBySnowflakeId(snowflakeId);
        addPoints(guildUser, 1L);
    }

    public void awardPoints(MessageReceivedEvent event) {
        User user = event.getAuthor();
        String snowflakeId = user.getId();
        String discordTag = user.getAsTag();

        if (!guildUserRepository.existsBySnowflakeId(user.getId())) {
            create(snowflakeId, discordTag);
        }
        GuildUser guildUser = guildUserRepository.getGuildUserBySnowflakeId(snowflakeId);
        addPoints(guildUser, 1L);
    }

    public void awardPoints(String snowflakeId, Long amount) {
        addPoints(getGuildUserBySnowflakeId(snowflakeId), amount);
    }

    private void addPoints(GuildUser user, Long amount) {
        user.addPoints(amount);
        guildUserRepository.update(user);
    }

    private void removePoints(GuildUser user, Long amount) {
        user.subtractPoints(amount);
        guildUserRepository.update(user);
    }

    private void removePoints(String userSnowflakeId, Long amount) {
        removePoints(guildUserRepository.getGuildUserBySnowflakeId(userSnowflakeId), amount);
    }

    private void create(String snowflakeId, String discordTag) {
        GuildUser user = new GuildUser();

        user.setSnowflakeId(snowflakeId);
        user.setDiscordTag(discordTag);
        user.setPoints(0L);

        guildUserRepository.save(user);
    }

    public GuildUser getGuildUserBySnowflakeId(String snowflakeId) {
        return guildUserRepository.getGuildUserBySnowflakeId(snowflakeId);
    }

    public Long getPointsBySnowflakeId(String snowflake) {
        GuildUser guildUser = getGuildUserBySnowflakeId(snowflake);
        return guildUser.getPoints();
    }

    public void save(GuildUser guildUser) {
        guildUserRepository.save(guildUser);
    }

    public void update(GuildUser guildUser) {
        guildUserRepository.update(guildUser);
    }
}
