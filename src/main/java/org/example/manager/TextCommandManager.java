package org.example.manager;

import org.example.textcommand.*;

import java.util.ArrayList;
import java.util.List;

public class TextCommandManager {
    private final List<TextCommand> textCommands = new ArrayList<>();

    public TextCommandManager() {
        register(new HelloCommand());
        register(new PolicjaCommand());
        register(new DogPicCommand());
    }

    public List<TextCommand> getTextCommands() {
        return textCommands;
    }
    private void register(TextCommand command) {
        textCommands.add(command);
    }
}
