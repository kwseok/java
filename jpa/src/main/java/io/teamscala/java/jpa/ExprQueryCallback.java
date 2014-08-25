package io.teamscala.java.jpa;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.path.PathBuilder;
import io.teamscala.java.core.data.Expr;

public interface ExprQueryCallback {
	<T> boolean before(JPQLQuery query, PathBuilder<?> path, String propertyName, Expr<T> expr);
	<T> void after(JPQLQuery query, PathBuilder<?> path, String propertyName, Expr<T> expr);
}
