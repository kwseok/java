package io.teamscala.java.core.data;

public abstract class ExprSqlCallbackAdapter implements ExprSqlCallback {
    @Override public <T> boolean before(StringBuilder builder, String propertyName, Expr<T> expr) { return false; }
    @Override public <T> void after(StringBuilder builder, String propertyName, Expr<T> expr) {}
}
