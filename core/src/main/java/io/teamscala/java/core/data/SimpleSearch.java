package io.teamscala.java.core.data;

import io.teamscala.java.core.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * 검색에 필요한 정보전달을 위한 간단한 구현이다.
 *
 */
public class SimpleSearch implements Searchable {

    // Static constants

    /** 검색 조건의 구분자들. */
    public static final String CONDITION_DELIMITERS = ", ";

    /** 기본 검색어 구분자들. */
    public static final String DEFAULT_KEYWORD_DELIMITERS = "\t\n\r\f ,-/:;#%";

    /** 검색 조건들 간의 기본 결합자. */
    public static final Junction DEFAULT_CONDITION_JUNCTION = Junction.OR;

    /** 검색어들 간의 기본 결합자. */
    public static final Junction DEFAULT_KEYWORD_JUNCTION = Junction.OR;

    /** 기본 연산자. */
    public static final Operator DEFAULT_OPERATOR = Operator.ANYWHERE;

    // Enumerations

    public enum Junction { OR, AND }
    public enum Operator {
        EXACT,
        START {
            @Override public String toMatchString(String pattern) { return '%' + pattern; }
        },
        END {
            @Override public String toMatchString(String pattern) { return pattern + '%'; }
        },
        ANYWHERE {
            @Override public String toMatchString(String pattern) { return '%' + pattern + '%'; }
        };

        public String toMatchString(String pattern) { return pattern; }
    }

    // Fields

    private String keywordDelimiters = DEFAULT_KEYWORD_DELIMITERS;

    private Junction conditionJunction = DEFAULT_CONDITION_JUNCTION;
    private Junction keywordJunction = DEFAULT_KEYWORD_JUNCTION;

    private Operator operator = DEFAULT_OPERATOR;

    private boolean ignoreCase;

    private String condition;
    private String keyword;

    // Getters and Setters...

    /**
     * 검색어 구분자들을 반환한다.
     *
     * @return 검색어 구분자들
     */
    public String getKeywordDelimiters() { return keywordDelimiters; }

    /**
     * 검색어 구분자들을 세팅한다.
     *
     * @param keywordDelimiters 검색어 구분자들
     */
    public void setKeywordDelimiters(String keywordDelimiters) {
        this.keywordDelimiters = keywordDelimiters;
    }

    /**
     * 검색 조건들 간의 결합자를 반환한다.
     *
     * @return 검색 조건들 간의 결합자
     */
    public Junction getConditionJunction() { return conditionJunction; }

    /**
     * 검색 조건들 간의 결합자를 세팅한다.
     *
     * @param conditionJunction 검색 조건들 간의 결합자
     */
    public void setConditionJunction(Junction conditionJunction) {
        if (conditionJunction == null)
            throw new IllegalArgumentException("ConditionJunction must not be null");

        this.conditionJunction = conditionJunction;
    }

    /**
     * 검색 조건들 간의 결합자를 세팅한다.
     *
     * @param conditionJunction 문자열 형식의 결합자
     */
    public void setConditionJunction(String conditionJunction) {
        this.conditionJunction = convertJunction(conditionJunction);
    }

    /**
     * 검색어들 간의 결합자를 반환한다.
     *
     * @return 검색어들 간의 결합자를
     */
    public Junction getKeywordJunction() { return keywordJunction; }

    /**
     * 검색어들 간의 결합자를 세팅한다.
     *
     * @param keywordJunction 검색어들 간의 결합자
     */
    public void setKeywordJunction(Junction keywordJunction) {
        if (keywordJunction == null)
            throw new IllegalArgumentException("KeywordJunction must not be null");

        this.keywordJunction = keywordJunction;
    }

    /**
     * 검색어들 간의 결합자를 세팅한다.
     *
     * @param keywordJunction 문자열 형식의 결합자
     * @see Junction
     */
    public void setKeywordJunction(String keywordJunction) {
        this.keywordJunction = convertJunction(keywordJunction);
    }

    /**
     * 연산자를 반환한다.
     * 기본값은 {@link #DEFAULT_OPERATOR}
     *
     * @return 검색어들 간의 연산자
     */
    public Operator getOperator()     { return operator; }

    /**
     * 연산자를 세팅한다.
     * 기본값은 {@link #DEFAULT_OPERATOR}
     *
     * @param operator 검색어들 간의 연산자
     */
    public void setOperator(Operator operator) {
        if (operator == null)
            throw new IllegalArgumentException("'operator' must not be null");

        this.operator = operator;
    }

    /**
     * 문자열 형식의 연산자를 세팅한다.
     *
     * @param operator 문자열 형식의 연산자
     * @see Operator
     */
    public void setOperator(String operator) { this.operator = convertOperator(operator); }

    public boolean isIgnoreCase() { return ignoreCase; }

    public void setIgnoreCase(boolean ignoreCase) { this.ignoreCase = ignoreCase; }

    /**
     * 검색 조건을 반환한다.
     *
     * @return 검색 조건
     */
    public String getCondition() { return condition; }

    /**
     * 검색 조건을 세팅한다.
     *
     * @param condition 검색 조건(공백 또는 콤마(,)로 구분)
     */
    public void setCondition(String condition) { this.condition = condition; }

    /**
     * 검색어를 반환한다.
     *
     * @return 검색어
     */
    public String getKeyword() { return keyword; }

    /**
     * 검색어를 세팅한다.
     *
     * @param keyword 검색어
     */
    public void setKeyword(String keyword) { this.keyword = keyword; }

    // Implementations for Object

    @Override public String toString() { return ToStringBuilder.reflectionToString(this); }

    // Operation methods

    /**
     * @return 검색이 가능한지 확인한다.
     */
    public boolean isSearchable() { return (hasCondition() && hasKeyword()); }

    /**
     * @return 검색 조건 존재 여부를 확인한다.
     */
    public boolean hasCondition() { return StringUtils.isNotBlank(condition); }

    /**
     * @return 검색어 존재 여부를 확인한다.
     */
    public boolean hasKeyword() { return StringUtils.isNotBlank(keyword); }

    /**
     * 검색 조건을 검사 한다.
     *
     * @param allowedConditions 허용된 검색 조건(콤마 또는 공백으로 구분)
     */
    public void checkConditions(String... allowedConditions) {
        checkConditions(Arrays.asList(allowedConditions));
    }

    /**
     * 검색 조건을 검사 한다.
     *
     * @param allowedConditions 허용된 검색 조건(콤마 또는 공백으로 구분)
     */
    public void checkConditions(Collection<String> allowedConditions) {
        if (allowedConditions == null || allowedConditions.isEmpty())
            throw new IllegalArgumentException("'allowedConditions' must not be empty");

        if (hasCondition()) {
            StringTokenizer condToken = new StringTokenizer(getCondition(), CONDITION_DELIMITERS);
            while (condToken.hasMoreTokens()) {
                String condition = condToken.nextToken();
                if (!allowedConditions.contains(condition))
                    throw new IllegalArgumentException("Invalid search condition [" + condition + "]");
            }
        }
    }

    /**
     * @return 검색 조건을 파싱하여 배열로 반환한다.
     */
    public String[] parseCondition() {
        if (!hasCondition())
            return ArrayUtils.EMPTY_STRING_ARRAY;

        return StringUtils.split(condition, CONDITION_DELIMITERS);
    }

    /**
     * @return 검색어를 파싱하여 배열로 반환한다.
     */
    public String[] parseKeyword() {
        if (!hasKeyword())
            return ArrayUtils.EMPTY_STRING_ARRAY;

        String[] parsedKeywords = StringUtils.split(keyword, keywordDelimiters);
        this.setKeyword(StringUtils.join(parsedKeywords, ' '));
        return parsedKeywords;
    }

    // Private

    private Junction convertJunction(String junction) {
        try {
            return Junction.valueOf(junction.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Illegal junction: '%s' (available to %s)",
                            junction, StringUtils.join(Junction.values(), ", ")),
                    e);
        }
    }

    private Operator convertOperator(String operator) {
        try {
            return Operator.valueOf(operator.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Illegal operator: '%s' (available to %s)",
                            operator, StringUtils.join(Operator.values(), ", ")),
                    e);
        }
    }
}