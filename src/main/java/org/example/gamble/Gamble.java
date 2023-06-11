package org.example.gamble;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;

import java.util.*;

public class Gamble {

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getId);

    @Getter
    private final Map<User, Integer> betsByUsers = Collections.synchronizedMap(new TreeMap<>(USER_COMPARATOR));

    public boolean addGambler(User user, int bet) {
        if (getBetForUser(user) >= bet) {
            return false;
        }

        betsByUsers.put(user, bet);
        return true;
    }

    public double getBetForUser(User user) {
        return betsByUsers.getOrDefault(user, 0);
    }

    public List<User> getParticipants() {
        return betsByUsers.keySet()
                .stream()
                .toList();
    }

    public long getTotalBet() {
        return betsByUsers.values()
                .stream()
                .mapToLong(i -> i)
                .sum();
    }
}
