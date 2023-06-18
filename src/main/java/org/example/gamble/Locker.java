package org.example.gamble;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Locker<T> {

    private final Comparator<T> resourceComparator;

    private final Set<T> lockedResources;

    public Locker(Comparator<T> resourceComparator) {
        this.resourceComparator = resourceComparator;
        this.lockedResources = new TreeSet<>(resourceComparator);
    }

    public synchronized boolean isLocked(T resource) {
        return lockedResources.stream()
                .anyMatch(lockedResource -> resourceComparator.compare(lockedResource, resource) == 0);
    }

    public synchronized void lock(T resource) {
        lockedResources.add(resource);
    }

    public synchronized void unlock(T resource) {
        lockedResources.remove(resource);
    }
}
