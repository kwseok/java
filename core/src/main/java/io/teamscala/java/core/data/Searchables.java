package io.teamscala.java.core.data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 여러개의 {@link Searchable}을 묶어서 처리할때 사용한다.
 */
public class Searchables implements Searchable, Iterable<Searchable> {

    private List<Searchable> searchables;

    public Searchables() {
        this(10);
    }

    public Searchables(int initialCapacity) {
        this.searchables = new ArrayList<>(initialCapacity);
    }

    public Searchables(Searchable... searchables) {
        this(Arrays.asList(searchables));
    }

    public Searchables(Collection<Searchable> searchables) {
        this(searchables != null ? searchables.size() : 10);
        if (searchables != null) {
            this.searchables.addAll(searchables.stream().filter(s -> s != null).collect(Collectors.toList()));
        }
    }

    public Searchables add(Searchable searchable) {
        this.searchables.add(searchable);
        return this;
    }

    @Override
    public Iterator<Searchable> iterator() {
        return searchables.iterator();
    }

    @Override
    public String toString() {
        return searchables.toString();
    }
}
