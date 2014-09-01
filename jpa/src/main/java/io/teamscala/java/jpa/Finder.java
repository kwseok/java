package io.teamscala.java.jpa;

import com.google.common.base.CaseFormat;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.QueryModifiers;
import com.mysema.query.ResultTransformer;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.*;
import com.mysema.query.types.path.PathBuilder;
import io.teamscala.java.core.data.PageResponse;
import io.teamscala.java.core.data.Pageable;
import io.teamscala.java.core.data.Searchable;
import io.teamscala.java.core.data.Sort;
import org.springframework.util.Assert;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * Finder for JPQL queries.
 */
public class Finder<ID, M extends Model<ID>> implements JPQLQuery {

    transient EntityManager entityManager;
    private final Class<M> type;
    private final PathBuilder<M> path;
    private final List<SearchListener<? extends Searchable>> searchListeners;

    /**
     * Creates a finder for entity of type <code>M</code> with search listeners.
     *
     * @param type            the type.
     * @param searchListeners the search listeners.
     */
    @SafeVarargs
    public Finder(Class<M> type, SearchListener<? extends Searchable>... searchListeners) {
        this(type, Arrays.asList(searchListeners));
    }

    /**
     * Creates a finder for entity of type <code>M</code> with search listeners.
     *
     * @param type            the type.
     * @param searchListeners the search listeners.
     */
    public Finder(Class<M> type, Collection<SearchListener<? extends Searchable>> searchListeners) {
        this((EntityManager) null, type, searchListeners);
    }

    /**
     * Creates a finder for entity of type <code>M</code> with search listeners, using a specific entity manager.
     *
     * @param entityManager   the entity manager.
     * @param type            the type.
     * @param searchListeners the search listeners.
     */
    @SafeVarargs
    public Finder(EntityManager entityManager, Class<M> type, SearchListener<? extends Searchable>... searchListeners) {
        this(entityManager, type, Arrays.asList(searchListeners));
    }

    /**
     * Creates a finder for entity of type <code>M</code> with search listeners, using a specific entity manager.
     *
     * @param entityManager   the entity manager.
     * @param type            the type.
     * @param searchListeners the search listeners.
     */
    public Finder(EntityManager entityManager, Class<M> type, Collection<SearchListener<? extends Searchable>> searchListeners) {
        Assert.notNull(type, "Type must not be null");

        this.entityManager = entityManager;
        this.type = type;
        this.path = new PathBuilder<>(type, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, type.getSimpleName()));
        this.searchListeners = new ArrayList<>((searchListeners != null ? searchListeners.size() : 0));
        if (searchListeners != null) this.searchListeners.addAll(searchListeners);
    }

    /**
     * @return Get the entity manager.
     */
    public EntityManager entityManager() {
        if (entityManager == null) {
            entityManager = JpaHelper.getEntityManager(type);
        }
        return entityManager;
    }

    /**
     * Changes the entity manager.
     *
     * @param entityManager the entity manager.
     * @return this
     */
    public Finder<ID, M> on(EntityManager entityManager) {
        return new Finder<>(entityManager, type, searchListeners);
    }

    /**
     * @return the path builder.
     */
    public PathBuilder<M> path() {
        return path;
    }

    /**
     * Register Search Listener.
     *
     * @param listener the search listener.
     * @return this
     */
    public Finder<ID, M> registerSearchListener(SearchListener<? extends Searchable> listener) {
        this.searchListeners.add(listener);
        return this;
    }

    /**
     * @return Retrieves all entities of the given type.
     */
    public List<M> all() {
        return query().list(path);
    }

    /**
     * Retrieves an entity reference for this ID.
     *
     * @param id the identifier.
     * @return the entity reference.
     */
    public M ref(ID id) {
        return entityManager().getReference(type, id);
    }

    /**
     * Retrieves an entity by ID.
     *
     * @param id the identifier.
     * @return the entity.
     */
    public M byId(ID id) {
        return entityManager().find(type, id);
    }

    /**
     * Creates a search, using a specific {@link JPQLQuery}.
     *
     * @param query the {@link com.mysema.query.jpa.JPQLQuery}.
     * @return {@link io.teamscala.java.jpa.Searcher}
     */
    public Searcher<ID, M> search(JPQLQuery query) {
        return new Searcher<>(query, type, path, searchListeners);
    }

    /**
     * Creates a search by {@link Searchable}, using a specific {@link JPQLQuery}.
     *
     * @param query the {@link com.mysema.query.jpa.JPQLQuery}.
     * @param s     the searchable.
     * @return {@link io.teamscala.java.jpa.Searcher}
     */
    public Searcher<ID, M> search(JPQLQuery query, Searchable s) {
        return search(query).apply(s);
    }

    /**
     * Creates a search.
     *
     * @return {@link io.teamscala.java.jpa.Searcher}
     */
    public Searcher<ID, M> search() {
        return new Searcher<>(query(), type, path, searchListeners);
    }

    /**
     * Creates a search by {@link Searchable}.
     *
     * @param s the searchable.
     * @return {@link io.teamscala.java.jpa.Searcher}
     */
    public Searcher<ID, M> search(Searchable s) {
        return search().apply(s);
    }

    /**
     * Creates a search by {@link Sort}.
     *
     * @param sort the sort.
     * @return {@link io.teamscala.java.jpa.Searcher}
     */
    public Searcher<ID, M> sort(Sort sort) {
        return search().sort(sort);
    }

    /**
     * Creates a search by {@link Pageable}.
     *
     * @param pageable the pageable.
     * @return {@link io.teamscala.java.jpa.Searcher}
     */
    public Searcher<ID, M> paginate(Pageable pageable) {
        return search().paginate(pageable);
    }

    /**
     * Retrieves entities by {@link Pageable}.
     *
     * @param pageable the pageable.
     * @return {@link io.teamscala.java.core.data.PageResponse}
     */
    public PageResponse<M> findPaginatedList(Pageable pageable) {
        return search().findPaginatedList(pageable);
    }

    /**
     * Creates a search by since id and limit / max results.
     *
     * @param sinceId the since id.
     * @param limit   the limit.
     * @return {@link io.teamscala.java.core.data.PageResponse}
     */
    public Searcher<ID, M> limit(ID sinceId, int limit) {
        return search().limit(sinceId, limit);
    }

    /**
     * Creates a search by since id and limit / max results.
     *
     * @param sinceId   the since id.
     * @param limit     the limit.
     * @param ascending the ascending.
     * @return {@link io.teamscala.java.core.data.PageResponse}
     */
    public Searcher<ID, M> limit(ID sinceId, int limit, boolean ascending) {
        return search().limit(sinceId, limit, ascending);
    }

    /**
     * Creates a query.
     *
     * @return {@link com.mysema.query.jpa.impl.JPAQuery}
     */
    public JPAQuery query() {
        return new JPAQuery(entityManager()).from(path);
    }

    /**
     * Creates a subquery.
     *
     * @return {@link com.mysema.query.jpa.JPASubQuery}
     */
    public JPASubQuery subquery() {
        return new JPASubQuery().from(path);
    }

    @Nonnegative
    @Override public long count() { return query().count(); }
    @Override public boolean exists() { return query().exists(); }
    @Override public boolean notExists() { return query().notExists(); }
    @Override public CloseableIterator<Tuple> iterate(Expression<?>... args) { return query().iterate(args); }
    @Override public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) { return query().iterate(projection); }
    @Override public List<Tuple> list(Expression<?>... args) { return query().list(args); }
    @Override public <RT> List<RT> list(Expression<RT> projection) { return query().list(projection); }
    @Override public SearchResults<Tuple> listResults(Expression<?>... args) { return query().listResults(args); }
    @Override public <RT> SearchResults<RT> listResults(Expression<RT> projection) { return query().listResults(projection); }
    @Override public <K, V> Map<K, V> map(Expression<K> key, Expression<V> value) { return query().map(key, value); }
    @Nullable
    @Override public Tuple singleResult(Expression<?>... args) { return query().singleResult(args); }
    @Nullable
    @Override public <RT> RT singleResult(Expression<RT> projection) { return query().singleResult(projection); }
    @Override public <T> T transform(ResultTransformer<T> transformer) { return query().transform(transformer); }
    @Nullable
    @Override public Tuple uniqueResult(Expression<?>... args) { return query().uniqueResult(args); }
    @Nullable
    @Override public <RT> RT uniqueResult(Expression<RT> projection) { return query().uniqueResult(projection); }
    @Override public JPQLQuery from(EntityPath<?>... sources) { return query().from(sources); }
    @Override public <P> JPQLQuery innerJoin(EntityPath<P> target) { return query().innerJoin(target); }
    @Override public <P> JPQLQuery innerJoin(EntityPath<P> target, Path<P> alias) { return query().innerJoin(target, alias); }
    @Override public <P> JPQLQuery innerJoin(CollectionExpression<?, P> target) { return query().innerJoin(target); }
    @Override public <P> JPQLQuery innerJoin(CollectionExpression<?, P> target, Path<P> alias) { return query().innerJoin(target, alias); }
    @Override public <P> JPQLQuery innerJoin(MapExpression<?, P> target) { return query().innerJoin(target); }
    @Override public <P> JPQLQuery innerJoin(MapExpression<?, P> target, Path<P> alias) { return query().innerJoin(target, alias); }
    @Override public <P> JPQLQuery join(EntityPath<P> target) { return query().join(target); }
    @Override public <P> JPQLQuery join(EntityPath<P> target, Path<P> alias) { return query().join(target, alias); }
    @Override public <P> JPQLQuery join(CollectionExpression<?, P> target) { return query().join(target); }
    @Override public <P> JPQLQuery join(CollectionExpression<?, P> target, Path<P> alias) { return query().join(target, alias); }
    @Override public <P> JPQLQuery join(MapExpression<?, P> target) { return query().join(target); }
    @Override public <P> JPQLQuery join(MapExpression<?, P> target, Path<P> alias) { return query().join(target, alias); }
    @Override public <P> JPQLQuery leftJoin(EntityPath<P> target) { return query().leftJoin(target); }
    @Override public <P> JPQLQuery leftJoin(EntityPath<P> target, Path<P> alias) { return query().leftJoin(target, alias); }
    @Override public <P> JPQLQuery leftJoin(CollectionExpression<?, P> target) { return query().leftJoin(target); }
    @Override public <P> JPQLQuery leftJoin(CollectionExpression<?, P> target, Path<P> alias) { return query().leftJoin(target, alias); }
    @Override public <P> JPQLQuery leftJoin(MapExpression<?, P> target) { return query().leftJoin(target); }
    @Override public <P> JPQLQuery leftJoin(MapExpression<?, P> target, Path<P> alias) { return query().leftJoin(target, alias); }
    @Override public <P> JPQLQuery rightJoin(EntityPath<P> target) { return query().rightJoin(target); }
    @Override public <P> JPQLQuery rightJoin(EntityPath<P> target, Path<P> alias) { return query().rightJoin(target, alias); }
    @Override public <P> JPQLQuery rightJoin(CollectionExpression<?, P> target) { return query().rightJoin(target); }
    @Override public <P> JPQLQuery rightJoin(CollectionExpression<?, P> target, Path<P> alias) { return query().rightJoin(target, alias); }
    @Override public <P> JPQLQuery rightJoin(MapExpression<?, P> target) { return query().rightJoin(target); }
    @Override public <P> JPQLQuery rightJoin(MapExpression<?, P> target, Path<P> alias) { return query().rightJoin(target, alias); }
    @Override public <P> JPQLQuery fullJoin(EntityPath<P> target) { return query().fullJoin(target); }
    @Override public <P> JPQLQuery fullJoin(EntityPath<P> target, Path<P> alias) { return query().fullJoin(target, alias); }
    @Override public <P> JPQLQuery fullJoin(CollectionExpression<?, P> target) { return query().fullJoin(target); }
    @Override public <P> JPQLQuery fullJoin(CollectionExpression<?, P> target, Path<P> alias) { return query().fullJoin(target, alias); }
    @Override public <P> JPQLQuery fullJoin(MapExpression<?, P> target) { return query().fullJoin(target); }
    @Override public <P> JPQLQuery fullJoin(MapExpression<?, P> target, Path<P> alias) { return query().fullJoin(target, alias); }
    @Override public JPQLQuery on(Predicate... condition) { return query().on(condition); }
    @Override public JPQLQuery groupBy(Expression<?>... exprs) { return query().groupBy(exprs); }
    @Override public JPQLQuery having(Predicate... predicates) { return query().having(predicates); }
    @Override public JPQLQuery limit(@Nonnegative long limit) { return query().limit(limit); }
    @Override public JPQLQuery offset(@Nonnegative long offset) { return query().offset(offset); }
    @Override public JPQLQuery restrict(QueryModifiers modifiers) { return query().restrict(modifiers); }
    @Override public JPQLQuery orderBy(OrderSpecifier<?>... o) { return query().orderBy(o); }
    @Override public <T> JPQLQuery set(ParamExpression<T> param, T value) { return query().set(param, value); }
    @Override public JPQLQuery distinct() { return query().distinct(); }
    @Override public JPQLQuery where(Predicate... predicates) { return query().where(predicates); }
    @Override public JPQLQuery fetch() { return query().fetch(); }
    @Override public JPQLQuery fetchAll() { return query().fetchAll(); }
}
