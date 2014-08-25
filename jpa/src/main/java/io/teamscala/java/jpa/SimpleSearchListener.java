package io.teamscala.java.jpa;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.StringPath;
import io.teamscala.java.core.data.SimpleSearch;
import io.teamscala.java.core.data.SimpleSearch.Junction;
import io.teamscala.java.core.data.SimpleSearch.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 간단한 검색 조건 생성 리스너. 
 * 
 * @author 석기원
 */
public class SimpleSearchListener implements SearchListener<SimpleSearch> {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleSearchListener.class);

	private final Map<String, StringPath> conditions;

	public SimpleSearchListener() { this.conditions = new HashMap<>(); } public SimpleSearchListener(int initialCapacity) { this.conditions = new HashMap<>(initialCapacity); }
	public SimpleSearchListener(Map<String, StringPath> conditions) {
		Assert.notEmpty(conditions, "'conditions' must not be empty");
		this.conditions = new HashMap<>(conditions);
	}

	public SimpleSearchListener put(String condition, StringPath path) {
        this.conditions.put(condition, path);
        return this;
    }

	@Override
	public boolean onSearch(SimpleSearch s, JPQLQuery query) {
		if (s.isSearchable()) {
			s.checkConditions(conditions.keySet());

			BooleanBuilder predicates = new BooleanBuilder();
			final String[] keywords = s.parseKeyword();
			for (String condition : s.parseCondition()) {
				BooleanBuilder insidePredicates = new BooleanBuilder();

				StringExpression path = conditions.get(condition);
				for (String keyword : keywords) {
					BooleanExpression expr;

					if (s.isIgnoreCase()) {
						path = path.lower();
						keyword = keyword.toLowerCase();
					}

					if (Operator.EXACT == s.getOperator())
						expr = path.eq(keyword);
					else
						expr = path.like(s.getOperator().toMatchString(keyword));

					if (Junction.OR == s.getKeywordJunction())
						insidePredicates.or(expr);
					else
						insidePredicates.and(expr);
				}

				if (Junction.OR == s.getConditionJunction())
					predicates.or(insidePredicates);
				else
					predicates.and(insidePredicates);
			}

			LOG.debug("Generated simple search predicate : " + predicates.toString());

			query.where(predicates);
		}
		return true;
	}
}
