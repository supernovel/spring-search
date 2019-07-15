package com.pixup.admin.common.search;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class GenericSpecification<T> implements Specification<T> {
	private static final long serialVersionUID = 2932876331483080974L;
	private SpecSearchCriteria criteria;

	public GenericSpecification(final SpecSearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}

	public SpecSearchCriteria getCriteria() {
		return criteria;
	}

	@Override
	public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
		SearchOperation operation = criteria.getOperation();
		String type = root.get(criteria.getKey()).getJavaType().getSimpleName();
		Object value = criteria.getValue();
		Optional<Predicate> result = Optional.ofNullable(null);
		
		try{
			switch(type){
				case "LocalDate":
					result = this.toPredicateUseType(
						root.get(criteria.getKey()), 
						LocalDate.parse(value.toString()),
						this.criteria,
						builder
					);
				case "LocalDateTime":
					result = this.toPredicateUseType(
						root.get(criteria.getKey()), 
						LocalDateTime.parse(value.toString()),
						this.criteria,
						builder
					);
					break;
			}
		}catch(Exception e){
			log.error("쿼리 파싱 중 에러 발생.", e);
		}

		return result.orElseGet(() -> {
			switch (operation) {
				case EQUALITY:
					return builder.equal(root.get(criteria.getKey()), value);
				case NEGATION:
					return builder.notEqual(root.get(criteria.getKey()), value);
				case GREATER_THAN:
					return builder.greaterThan(root.get(criteria.getKey()), value.toString());
				case LESS_THAN:
					return builder.lessThan(root.get(criteria.getKey()), value.toString());
				case GREATER_THAN_EQUAL:
					return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), value.toString());
				case LESS_THAN_EQUAL:
					return builder.lessThanOrEqualTo(root.get(criteria.getKey()), value.toString());
				case LIKE:
					return builder.like(root.get(criteria.getKey()), value.toString());
				case STARTS_WITH:
					return builder.like(root.get(criteria.getKey()), value + "%");
				case ENDS_WITH:
					return builder.like(root.get(criteria.getKey()), "%" + value);
				case CONTAINS:
					return builder.like(root.get(criteria.getKey()), "%" + value + "%");
				default:
					return null;
			}
		});
	}

	private <U extends Comparable<? super U>> Optional<Predicate> toPredicateUseType(
		final Path<U> path, 
		U value, 
		final SpecSearchCriteria criteria,
		final CriteriaBuilder builder
	){
		Predicate predicate = null;

		switch (criteria.getOperation()) {
			case GREATER_THAN:
				predicate = builder.greaterThan(path, value);
				break;
			case LESS_THAN:
				predicate = builder.lessThan(path, value);
				break;
			case GREATER_THAN_EQUAL:
				predicate = builder.greaterThanOrEqualTo(path, value);
				break;
			case LESS_THAN_EQUAL:
				predicate = builder.lessThanOrEqualTo(path, value);
				break;
			default:
				break;
		}

		return Optional.ofNullable(predicate);
	}

	public static <S> Specification<S> of(String searchParams){
		CriteriaParser parser = new CriteriaParser();
		GenericSpecificationsBuilder<S> specBuilder = new GenericSpecificationsBuilder<>();
		return specBuilder.build(parser.parse(searchParams), GenericSpecification<S>::new);
	}
}
