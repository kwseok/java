package io.teamscala.java.core.data;

public interface ExprValueTransformer {
    <T> Object transform(Expr<T> expr);
}
