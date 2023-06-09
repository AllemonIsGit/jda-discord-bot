package org.example.manager;

import org.example.service.InsultService;
import org.example.textcommand.*;

import java.util.ArrayList;
import java.util.List;

public class TextCommandManager {
    private static TextCommandManager INSTANCE;
    private final List<TextCommand> textCommands = new ArrayList<>();

    public TextCommandManager(InsultService insultService) {
        register(new HelloCommand());
        register(new PolicjaCommand());
        register(new DogPicCommand());
        register(new InsultCommand(insultService));
    }

    public List<TextCommand> getTextCommands() {
        return textCommands;
    }
    private void register(TextCommand command) {
        textCommands.add(command);
    }

    public static TextCommandManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TextCommandManager(InsultService.getInstance());
            return INSTANCE;
        }
        return INSTANCE;
    }
}
