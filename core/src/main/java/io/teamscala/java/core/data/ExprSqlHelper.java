package io.teamscala.java.core.data;

import io.teamscala.java.core.util.StringUtils;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class ExprSqlHelper {

    public static final String DEFAULT_UPPER_FN = "upper";

    private final StringBuilder builder;
    private final Map<String, Object> params;
    private ExprSqlCallback callback;
    private String upperFn = DEFAULT_UPPER_FN;
    private Map<String, ExprValueTransformer> exprValueTransformers = new HashMap<>();

    public ExprSqlHelper() {
        this(new StringBuilder());
    }

    public ExprSqlHelper(StringBuilder builder) {
        Assert.notNull(builder, "Builder must not be null");
        this.builder = builder;
        this.params = new HashMap<>();
    }

    public ExprSqlHelper setUpperFn(String upperFn) {
        this.upperFn = StringUtils.defaultIfEmpty(upperFn, DEFAULT_UPPER_FN);
        return this;
    }

    public ExprSqlHelper setCallback(ExprSqlCallback callback) {
        this.callback = callback;
        return this;
    }

    public ExprSqlHelper registerExprValueTransformer(String propertyName, ExprValueTransformer transformer) {
        Assert.hasText(propertyName, "PropertyName must not be empty");
        Assert.notNull(transformer, "Transformer must not be empty");
        this.exprValueTransformers.put(propertyName, transformer);
        return this;
    }

    public String getSqlString() {
        return builder.toString();
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return builder.toString() + " @ " + params;
    }

    public <T> boolean apply(String propertyName, Expr<T> expr) {
        if (expr == null || expr.isEmpty()) return false;
        if (callback != null && !callback.before(builder, propertyName, expr)) return false;

        Object value;
        if (exprValueTransformers.containsKey(propertyName))
            value = exprValueTransformers.get(propertyName).transform(expr);
        else
            value = expr.val();

        builder.append(" and ");
        if (expr.isIgnoreCase())
            builder.append(upperFn).append("(").append(propertyName).append(")");
        else
            builder.append(propertyName);
        switch (expr.op()) {
            case LT: builder.append(" < "); break;
            case LE: builder.append(" <= "); break;
            case EQ: builder.append(" = "); break;
            case GE: builder.append(" >= "); break;
            case GT: builder.append(" > "); break;
            case NE: builder.append(" <> "); break;
            case LIKE:
                builder.append(" like ");
                if (expr.isIgnoreCase())
                    value = convertMatchMode(value.toString().toUpperCase(), expr.matchMode());
                else
                    value = convertMatchMode(value.toString(), expr.matchMode());
                break;
            default:
                throw new IllegalArgumentException("Unavailable operator : " + expr.op());
        }
        builder.append(":").append(propertyName);
        params.put(propertyName, value);

        if (callback != null) callback.after(builder, propertyName, expr);
        return true;
    }

    private String convertMatchMode(String value, Expr.MatchMode matchMode) {
        switch (matchMode) {
            case EXACT: return value;
            case START: return "%" + value;
            case END: return value + "%";
            case ANYWHERE: return "%" + value + "%";
        }
        throw new IllegalArgumentException("Unavailable match mode : " + matchMode);
    }
}
