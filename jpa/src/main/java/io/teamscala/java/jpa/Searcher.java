package io.teamscala.java.jpa;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.PathBuilder;
import io.teamscala.java.core.data.*;
import io.teamscala.java.core.data.Sort.Order;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import javax.persistence.metamodel.EntityType;
import java.util.*;

/**
 * Search for JPA queries.
 */
public class Searcher<ID, M extends Model<ID>> {

    private final JPQLQuery query;
    private final Class<M> type;
    private final PathBuilder<M> path;
    private final List<SearchListener<? extends Searchable>> listeners;

    /**
     * Creates a search for model of type <code>M</code> with listeners.
     *
     * @param query     the query.
     * @param type      the type.
     * @param path      the path.
     * @param listeners the search listeners.
     */
    @SafeVarargs
    public Searcher(JPQLQuery query, Class<M> type, PathBuilder<M> path, SearchListener<? extends Searchable>... listeners) {
        this(query, type, path, Arrays.asList(listeners));
    }

    /**
     * Creates a search for model of type <code>M</code> with listeners.
     *
     * @param query     the query.
     * @param type      the type.
     * @param path      the path.
     * @param listeners the search listeners.
     */
    public Searcher(JPQLQuery query, Class<M> type, PathBuilder<M> path, Collection<SearchListener<? extends Searchable>> listeners) {
        Assert.notNull(query, "Query must not be null");
        Assert.notNull(type, "Type must not be null");
        Assert.notNull(path, "Path must not be null");

        this.query = query;
        this.type = type;
        this.path = path;
        this.listeners = new ArrayList<>((listeners != null ? listeners.size() : 0));
        if (listeners != null) this.listeners.addAll(listeners);
    }

    /**
     * @return the query that owns this search.
     */
    public JPQLQuery query() {
        return query;
    }

    /**
     * @return the path builder.
     */
    public PathBuilder<M> path() {
        return path;
    }

    /**
     * Register Listener.
     *
     * @param listener the search listener.
     * @return this
     */
    public Searcher<ID, M> registerListener(SearchListener<? extends Searchable> listener) {
        this.listeners.add(listener);
        return this;
    }

    /**
     * Defines the {@link Searchable} for the search results
     *
     * @param s the searchable.
     * @return this
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Searcher<ID, M> apply(Searchable s) {
        if (s != null) {
            boolean fired = false;

            if (s instanceof Searchables) {
                fired = true;
                for (Searchable searchable : (Searchables) s) apply(searchable);
            }

            for (SearchListener listener : listeners) {
                Class<?> typeArgument = ResolvableType
                    .forClass(listener.getClass())
                    .as(SearchListener.class)
                    .resolveGenerics()[0];
                if (typeArgument != null && typeArgument.isAssignableFrom(s.getClass())) {
                    fired = true;
                    if (!listener.onSearch(s, query)) break;
                }
            }

            if (!fired) throw new IllegalArgumentException("Unsupported searchable " + s.getClass());
        }
        return this;
    }

    /**
     * Defines the {@link Sort} for the search results
     *
     * @param sort the sort.
     * @return this
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Searcher<ID, M> sort(Sort sort) {
        if (sort != null) {
            for (Order order : sort) {
                com.mysema.query.types.Order direction =
                    order.isAscending()
                        ? com.mysema.query.types.Order.ASC
                        : com.mysema.query.types.Order.DESC;
                Expression<?> property = path.get(order.getProperty());
                query.orderBy(new OrderSpecifier(direction, property));
            }
        }
        return this;
    }

    /**
     * Defines the {@link Pageable} for the search results
     *
     * @param pageable the pageable.
     * @return this
     */
    public Searcher<ID, M> paginate(Pageable pageable) {
        if (pageable != null) {
            final int offset = pageable.getOffset();
            final int limit = pageable.getSize();

            if (offset > 0) query.offset(offset);
            if (limit > 0) query.limit(limit);

            sort(pageable.getSort());
        }
        return this;
    }

    /**
     * Defines the since id and limit / max results for the search results
     *
     * @param sinceId the since id.
     * @param limit   the limit.
     * @return this
     */
    public Searcher<ID, M> limit(ID sinceId, int limit) {
        return limit(sinceId, limit, false);
    }

    /**
     * Defines the since id and limit / max results for the search results
     *
     * @param sinceId   the since id.
     * @param limit     the limit.
     * @param ascending the ascending.
     * @return this
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Searcher<ID, M> limit(ID sinceId, int limit, boolean ascending) {
        EntityType<M> entityType = JpaHelper.getEntityManager(type).getMetamodel().entity(type);
        Class<?> idType = entityType.getIdType().getJavaType();

        if (!Comparable.class.isAssignableFrom(idType)) {
            throw new IllegalStateException("Identifier cannot comparable type : " + idType);
        }

        ComparablePath<Comparable> idPath = path.getComparable(entityType.getId(idType).getName(), (Class<Comparable>) idType);

        if (sinceId != null) {
            if (ascending)
                query.where(idPath.gt((Comparable) sinceId));
            else
                query.where(idPath.lt((Comparable) sinceId));
        }

        if (limit > 0) query.limit(limit);

        query.orderBy(idPath.desc());
        return this;
    }

    /**
     * Returns a {@link PageResponse} for this search.
     *
     * @param pageable the pageable.
     * @return {@link io.teamscala.java.core.data.PageResponse}
     */
    public PageResponse<M> findPaginatedList(Pageable pageable) {
        List<M> list = Collections.emptyList();
        long total = query.count();
        if (total > 0) {
            paginate(pageable);
            list = query.list(path);
        }
        return new PageResponse<>(list, pageable, total);
    }
}
