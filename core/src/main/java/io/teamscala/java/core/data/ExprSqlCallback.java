package io.teamscala.java.core.data;

public interface ExprSqlCallback {
    <T> boolean before(StringBuilder builder, String propertyName, Expr<T> expr);
    <T> void after(StringBuilder builder, String propertyName, Expr<T> expr);
}
