package org.example.gamble;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;

import java.util.*;

@RequiredArgsConstructor
public class Gamble {

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getId);

    @Getter
    private final User initiator;

    @Getter
    private final Map<User, Long> betsByUsers = Collections.synchronizedMap(new TreeMap<>(USER_COMPARATOR));

    public boolean addGambler(User user, Long bet) {
        if (getBetForUser(user) >= bet) {
            return false;
        }

        betsByUsers.put(user, bet);
        return true;
    }

    public double getBetForUser(User user) {
        return betsByUsers.getOrDefault(user, 0L);
    }

    public List<User> getParticipants() {
        return betsByUsers.keySet()
                .stream()
                .toList();
    }

    public List<Participant> getLeaderboard() {
        return betsByUsers.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> new Participant(entry.getKey(), entry.getValue()))
                .toList();
    }

    public long getTotalBet() {
        return betsByUsers.values()
                .stream()
                .mapToLong(i -> i)
                .sum();
    }

    public record Participant(User user, Long bet) {
    }
}
