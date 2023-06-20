package org.example.service;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.domain.model.GuildUser;
import org.example.repository.GuildUserRepository;

public class GuildUserService {

    private static GuildUserService INSTANCE;
    private final GuildUserRepository guildUserRepository;

    private GuildUserService() {
        this.guildUserRepository = GuildUserRepository.getInstance();
    }

    public static GuildUserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GuildUserService();
            return INSTANCE;
        }
        return INSTANCE;
    }

    public void awardPoints(MessageReceivedEvent event) {
        User user = event.getAuthor();
        String snowflakeId = user.getId();
        String discordTag = user.getAsTag();

        if (!guildUserRepository.existsByDiscordId(user.getId())) {
            create(snowflakeId, discordTag);
        }
        GuildUser guildUser = guildUserRepository.getUserBySnowflakeId(snowflakeId);
        addPoints(guildUser, 1);
    }

    private void addPoints(GuildUser user, Integer amount) {
        user.setPoints(user.getPoints() + amount);
        guildUserRepository.update(user);
    }

    public void create(String snowflakeId, String discordTag) {
        GuildUser user = new GuildUser();

        user.setSnowflakeId(snowflakeId);
        user.setDiscordTag(discordTag);
        user.setPoints(0);

        guildUserRepository.save(user);
    }
}
