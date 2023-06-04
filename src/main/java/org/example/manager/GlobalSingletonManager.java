package org.example.manager;

import lombok.Getter;
import org.example.service.EventService;
import org.example.service.InsultService;

@Getter
public class GlobalSingletonManager {
    private final SlashCommandManager slashCommandManager;
    private final TextCommandManager textCommandManager;
    private final EventService eventService;
    private final InsultService insultService;

    public GlobalSingletonManager() {
        this.insultService = InsultService.getInstance();
        this.eventService = EventService.getInstance();

        this.slashCommandManager = SlashCommandManager.getInstance();
        this.textCommandManager = new TextCommandManager(insultService);
    }
}
