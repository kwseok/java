package io.teamscala.java.jpa;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;
import io.teamscala.java.core.data.Expr;
import io.teamscala.java.core.data.ExprValueTransformer;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class ExprQueryHelper {

	private final JPQLQuery query;
	private final PathBuilder<?> path;
	private ExprQueryCallback callback;
	private Map<String, ExprValueTransformer> exprValueTransformers = new HashMap<>();

	public ExprQueryHelper(JPQLQuery query, PathBuilder<?> path) {
		Assert.notNull(query, "Query must not be null");
		Assert.notNull(path, "Path must not be null");
		this.query = query;
		this.path = path;
	}

	public ExprQueryHelper setCallback(ExprQueryCallback callback) {
		this.callback = callback;
		return this;
	}

	public ExprQueryHelper registerExprValueTransformer(String propertyName, ExprValueTransformer transformer) {
		Assert.hasText(propertyName, "PropertyName must not be empty");
		Assert.notNull(transformer, "Transformer must not be empty");
		this.exprValueTransformers.put(propertyName, transformer);
		return this;
	}

	public <T> boolean apply(String propertyName, Expr<T> expr) {
		if (expr == null || expr.isEmpty()) return false;
		if (callback != null && !callback.before(query, path, propertyName, expr)) return false;

		Object value;
		if (exprValueTransformers.containsKey(propertyName))
			value = exprValueTransformers.get(propertyName).transform(expr);
		else
			value = expr.val();

		@SuppressWarnings("rawtypes")
		ComparablePath<Comparable> comparableProperty = path.getComparable(propertyName, Comparable.class);
		Comparable<?> comparableValue;
		if (value instanceof Comparable)
			comparableValue = (Comparable<?>) value;
		else
			throw new IllegalArgumentException("No comparable value : " + value.getClass());

        switch (expr.op()) {
            case LT: query.where(comparableProperty.lt(comparableValue)); break;
            case LE: query.where(comparableProperty.loe(comparableValue)); break;
            case EQ: query.where(comparableProperty.eq(comparableValue)); break;
            case GE: query.where(comparableProperty.goe(comparableValue)); break;
            case GT: query.where(comparableProperty.gt(comparableValue)); break;
            case NE: query.where(comparableProperty.ne(comparableValue)); break;
            case LIKE:
                StringPath stringProperty = path.getString(propertyName);
                String convertedValue = convertMatchMode(value.toString(), expr.matchMode());
                if (expr.isIgnoreCase())
                    query.where(stringProperty.toUpperCase().like(convertedValue.toUpperCase()));
                else
                    query.where(stringProperty.like(convertedValue));
                break;
            default:
                throw new IllegalArgumentException("Unavailable operator : " + expr.op());
        }

		if (callback != null) callback.after(query, path, propertyName, expr);
		return true;
	}

	private String convertMatchMode(String value, Expr.MatchMode matchMode) {
		switch (matchMode) {
            case EXACT: return value;
            case START: return "%" + value;
            case END: return value + "%";
            case ANYWHERE: return "%" + value + "%";
            default:
                throw new IllegalArgumentException("Unavailable match mode : " + matchMode);
		}
	}
}