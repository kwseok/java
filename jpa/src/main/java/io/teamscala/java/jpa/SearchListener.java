package io.teamscala.java.jpa;

import com.mysema.query.jpa.JPQLQuery;
import io.teamscala.java.core.data.Searchable;

/**
 * Search Listener.
 *
 * @param <S> The searchable type
 */
public interface SearchListener<S extends Searchable> {

    /**
     * 이벤트
     *
     * @param s     the search conditions.
     * @param query {@link JPQLQuery}
     * @return true or false.
     */
    boolean onSearch(S s, JPQLQuery query);
}
