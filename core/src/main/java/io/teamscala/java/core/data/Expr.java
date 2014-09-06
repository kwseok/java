package io.teamscala.java.core.data;

import org.springframework.util.Assert;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class Expr<T> {

    // Enums

    public enum Operator {LT, LE, EQ, GE, GT, NE, LIKE}

    public enum MatchMode {EXACT, START, END, ANYWHERE}

    // Fields

    private T val;
    private Operator op;
    private MatchMode matchMode;
    private boolean ignoreCase;

    // Constructors

    public Expr() {
        this(Operator.EQ);
    }

    public Expr(Operator op) {
        this.op(op);
    }

    public Expr(MatchMode matchMode) {
        this.matchMode(matchMode);
    }

    // Getters and Setters...

    public boolean isDefined() {
        return (val != null) && (!(val instanceof CharSequence) || isNotBlank(val.toString()));
    }

    public boolean isEmpty() {
        return !isDefined();
    }

    public T val() {
        return val;
    }

    public Expr<T> val(T val) {
        this.val = val;
        return this;
    }

    public T getVal() {
        return this.val();
    }

    public void setVal(T val) {
        this.val(val);
    }

    public Operator op() {
        return op;
    }

    public Expr<T> op(Operator op) {
        Assert.notNull(op, "Operator must not be null");
        this.op = op;
        this.matchMode = op == Operator.LIKE ? MatchMode.START : MatchMode.EXACT;
        return this;
    }

    public Operator getOp() {
        return this.op();
    }

    public void setOp(Operator op) {
        this.op(op);
    }

    public MatchMode matchMode() {
        return matchMode;
    }

    public Expr<T> matchMode(MatchMode matchMode) {
        Assert.notNull(matchMode, "MatchMode must not be null");
        this.matchMode = matchMode;
        this.op = Operator.LIKE;
        return this;
    }

    public MatchMode getMatchMode() {
        return this.matchMode();
    }

    public void setMatchMode(MatchMode matchMode) {
        this.matchMode(matchMode);
    }

    public boolean ignoreCase() {
        return ignoreCase;
    }

    public Expr<T> ignoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    public boolean isIgnoreCase() {
        return this.ignoreCase();
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase(ignoreCase);
    }

    // Override for Object

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
