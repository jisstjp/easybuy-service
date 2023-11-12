package com.stock.inventorymanagement.dao.impl;

import com.stock.inventorymanagement.dao.ProductDao;
import com.stock.inventorymanagement.domain.Product;
import com.stock.inventorymanagement.dto.FieldCriterion;
import com.stock.inventorymanagement.dto.ProductDto;
import com.stock.inventorymanagement.dto.ProductSearchCriteria;
import com.stock.inventorymanagement.dto.SortCriteria;
import com.stock.inventorymanagement.enums.Operator;
import com.stock.inventorymanagement.enums.PriceType;
import com.stock.inventorymanagement.enums.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.ManagedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     *
     * public Page<Product> searchProducts(ProductSearchCriteria searchCriteria,
     * Pageable pageable) { CriteriaBuilder criteriaBuilder =
     * entityManager.getCriteriaBuilder(); CriteriaQuery<Product> query =
     * criteriaBuilder.createQuery(Product.class); Root<Product> root =
     * query.from(Product.class); query.select(root); List<Predicate> predicates =
     * new ArrayList<>();
     *
     * if (searchCriteria.getKeyword() != null) { String keyword = "%" +
     * searchCriteria.getKeyword() + "%"; Predicate namePredicate =
     * criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
     * keyword.toLowerCase()); Predicate descriptionPredicate =
     * criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
     * keyword.toLowerCase()); predicates.add(criteriaBuilder.or(namePredicate,
     * descriptionPredicate)); }
     *
     * if (searchCriteria.getFieldCriteria() != null) { for (Map.Entry<String,
     * FieldCriterion<?>> entry : searchCriteria.getFieldCriteria().entrySet()) {
     * String field = entry.getKey(); FieldCriterion<?> fieldCriterion =
     * entry.getValue(); predicates.add(getPredicateForField(root, criteriaBuilder,
     * field, fieldCriterion)); } }
     *
     * query.where(predicates.toArray(new Predicate[0]));
     *
     * if (searchCriteria.getSortCriteria() != null) { SortCriteria sortCriteria =
     * searchCriteria.getSortCriteria(); String sortBy = sortCriteria.getSortBy();
     * SortDirection sortDirection = sortCriteria.getSortDirection();
     *
     * if (sortBy != null && sortDirection != null) { if (sortDirection ==
     * SortDirection.ASC) { query.orderBy(criteriaBuilder.asc(root.get(sortBy))); }
     * else { query.orderBy(criteriaBuilder.desc(root.get(sortBy))); } } }
     *
     * TypedQuery<Product> typedQuery = entityManager.createQuery(query);
     * typedQuery.setFirstResult((int) pageable.getOffset());
     * typedQuery.setMaxResults(pageable.getPageSize());
     *
     * List<Product> products = typedQuery.getResultList(); long total =
     * getTotalCount(searchCriteria);
     *
     * return new PageImpl<>(products, pageable, total); }
     *
     * private Predicate getPredicateForField(Root<Product> root, CriteriaBuilder
     * criteriaBuilder, String fieldName, FieldCriterion<?> fieldCriterion) {
     * String[] fieldParts = fieldName.split("\\."); Path<?> fieldPath = root;
     *
     * for (String field : fieldParts) { if (isNestedEntity(fieldPath, field)) {
     * fieldPath = getNestedEntityJoin(fieldPath, field); } else { fieldPath =
     * getFieldPath(fieldPath, field); } }
     *
     * return createPredicateForOperator(criteriaBuilder, fieldPath,
     * fieldCriterion); }
     *
     * @Override public Long getTotalCount(ProductSearchCriteria searchCriteria) {
     * CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
     * CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
     * Root<Product> root = query.from(Product.class);
     * query.select(criteriaBuilder.count(root));
     *
     * List<Predicate> predicates = buildPredicates(searchCriteria, root,
     * criteriaBuilder); query.where(predicates.toArray(new Predicate[0]));
     *
     * TypedQuery<Long> typedQuery = entityManager.createQuery(query); return
     * typedQuery.getSingleResult(); }
     *
     * private List<Predicate> buildPredicates(ProductSearchCriteria searchCriteria,
     * Root<Product> root, CriteriaBuilder criteriaBuilder) { List<Predicate>
     * predicates = new ArrayList<>();
     *
     * if (searchCriteria.getKeyword() != null) { String keyword = "%" +
     * searchCriteria.getKeyword() + "%"; Predicate namePredicate =
     * criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
     * keyword.toLowerCase()); Predicate descriptionPredicate =
     * criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
     * keyword.toLowerCase()); predicates.add(criteriaBuilder.or(namePredicate,
     * descriptionPredicate)); }
     *
     * if (searchCriteria.getFieldCriteria() != null) { for (Map.Entry<String,
     * FieldCriterion<?>> entry : searchCriteria.getFieldCriteria().entrySet()) {
     * String fieldName = entry.getKey(); FieldCriterion<?> fieldCriterion =
     * entry.getValue(); Predicate predicate = getPredicateForField(root,
     * criteriaBuilder, fieldName, fieldCriterion); predicates.add(predicate); } }
     *
     * return predicates; }
     *
     * @SuppressWarnings({ "rawtypes", "unchecked" }) private Predicate
     * createPredicateForOperator(CriteriaBuilder criteriaBuilder, Path<?>
     * fieldPath, FieldCriterion<?> fieldCriterion) { Operator operator =
     * fieldCriterion.getOperator(); Object value = fieldCriterion.getValue();
     *
     * switch (operator) { case EQUAL: return criteriaBuilder.equal(fieldPath,
     * value); case NOT_EQUAL: return criteriaBuilder.notEqual(fieldPath, value);
     * case GREATER_THAN: return criteriaBuilder.greaterThan((Expression<? extends
     * Comparable>) fieldPath, (Comparable) value); case LESS_THAN: return
     * criteriaBuilder.lessThan((Expression<? extends Comparable>) fieldPath,
     * (Comparable) value); case GREATER_THAN_OR_EQUAL: return
     * criteriaBuilder.greaterThanOrEqualTo((Expression<? extends Comparable>)
     * fieldPath, (Comparable) value); case LESS_THAN_OR_EQUAL: return
     * criteriaBuilder.lessThanOrEqualTo((Expression<? extends Comparable>)
     * fieldPath, (Comparable) value); case CONTAINS: return
     * criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath),
     * "%" + value.toString().toLowerCase() + "%"); case STARTS_WITH: return
     * criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath),
     * value.toString().toLowerCase() + "%"); case ENDS_WITH: return
     * criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath),
     * "%" + value.toString().toLowerCase()); default: throw new
     * IllegalArgumentException("Unsupported operator: " + operator); } }
     *
     * private Path<?> getFieldPath(Path<?> rootPath, String fieldName) { String[]
     * fieldParts = fieldName.split("\\.");
     *
     * Path<?> fieldPath = rootPath; for (String field : fieldParts) { if
     * (isNestedEntity(fieldPath, field)) { fieldPath =
     * getNestedEntityJoin(fieldPath, field); } else { fieldPath =
     * fieldPath.get(field); } }
     *
     * return fieldPath; }
     *
     * private boolean isNestedEntity(Path<?> rootPath, String fieldName) { return
     * rootPath.get(fieldName).getModel() instanceof EntityType<?>; }
     *
     * private Join<?, ?> getNestedEntityJoin(Path<?> rootPath, String fieldName) {
     * if (rootPath instanceof Join) { Join<?, ?> join = (Join<?, ?>) rootPath;
     * return join.join(fieldName, JoinType.INNER); } else if (rootPath instanceof
     * From) { From<?, ?> from = (From<?, ?>) rootPath; return from.join(fieldName,
     * JoinType.INNER); }
     *
     * throw new IllegalArgumentException("Invalid field: " + fieldName); }
     */

    public Page<Product> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if (searchCriteria.getKeyword() != null) {
            String keyword = "%" + searchCriteria.getKeyword() + "%";
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    keyword.toLowerCase());
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    keyword.toLowerCase());
            predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
        }

        if (searchCriteria.getFieldCriteria() != null) {
            for (FieldCriterion<?> fieldCriterion : searchCriteria.getFieldCriteria()) {
                predicates.add(getPredicateForField(root, criteriaBuilder, fieldCriterion));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (searchCriteria.getSortCriteria() != null) {
            SortCriteria sortCriteria = searchCriteria.getSortCriteria();
            String sortBy = sortCriteria.getSortBy();
            SortDirection sortDirection = sortCriteria.getSortDirection();

            if (sortBy != null && sortDirection != null) {
                if (sortDirection == SortDirection.ASC) {
                    query.orderBy(criteriaBuilder.asc(root.get(sortBy)));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get(sortBy)));
                }
            }
        }

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Product> products = typedQuery.getResultList();
        long total = getTotalCount(searchCriteria);

        return new PageImpl<>(products, pageable, total);
    }

    private Predicate getPredicateForField(Root<Product> root, CriteriaBuilder criteriaBuilder,
                                           FieldCriterion<?> fieldCriterion) {
        String fieldName = fieldCriterion.getField();
        Object value = fieldCriterion.getValue();
        Operator operator = fieldCriterion.getOperator();

        String[] fieldParts = fieldName.split("\\.");
        Path<?> fieldPath = root;

        for (String field : fieldParts) {
            if (isNestedEntity(fieldPath, field)) {
                fieldPath = getNestedEntityJoin(fieldPath, field);
            } else {
                fieldPath = getFieldPath(fieldPath, field);
            }
        }

        return createPredicateForOperator(criteriaBuilder, fieldPath, operator, value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Predicate createPredicateForOperator(CriteriaBuilder criteriaBuilder, Path<?> fieldPath, Operator operator,
                                                 Object value) {
        switch (operator) {
            case EQUALS:
                return criteriaBuilder.equal(fieldPath, value);
            case NOT_EQUALS:
                return criteriaBuilder.notEqual(fieldPath, value);
            case GREATER_THAN:
                return criteriaBuilder.greaterThan((Expression<Comparable>) fieldPath, (Comparable) value);
            case LESS_THAN:
                return criteriaBuilder.lessThan((Expression<Comparable>) fieldPath, (Comparable) value);
            case GREATER_THAN_OR_EQUALS:
                return criteriaBuilder.greaterThanOrEqualTo((Expression<Comparable>) fieldPath, (Comparable) value);
            case LESS_THAN_OR_EQUALS:
                return criteriaBuilder.lessThanOrEqualTo((Expression<Comparable>) fieldPath, (Comparable) value);
            case CONTAINS:
                return criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath),
                        "%" + value.toString().toLowerCase() + "%");
            case STARTS_WITH:
                return criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath),
                        value.toString().toLowerCase() + "%");
            case ENDS_WITH:
                return criteriaBuilder.like(criteriaBuilder.lower((Expression<String>) fieldPath),
                        "%" + value.toString().toLowerCase());

            case IN_OPERATOR:
                CriteriaBuilder.In<Object> inClause = criteriaBuilder.in(fieldPath);
                // Assuming value is a comma-separated string.
                String[] values = value.toString().split(",");
                for (String val : values) {
                    Object convertedValue = convertStringToFieldType(fieldPath.getJavaType(), val.trim());
                    inClause.value(convertedValue); // Adding each trimmed value to the IN clause
                }
                return inClause;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    private boolean isNestedEntity(Path<?> rootPath, String fieldName) {
        if (rootPath.get(fieldName).getModel() instanceof ManagedType<?>) {
            return true;
        }
        return false;
    }

    private Path<?> getFieldPath(Path<?> rootPath, String fieldName) {
        String[] fieldParts = fieldName.split("\\.");

        Path<?> fieldPath = rootPath;
        for (String field : fieldParts) {
            if (isNestedEntity(fieldPath, field)) {
                fieldPath = getNestedEntityJoin(fieldPath, field);
            } else {
                fieldPath = fieldPath.get(field);
            }
        }

        return fieldPath;
    }

    private Join<?, ?> getNestedEntityJoin(Path<?> rootPath, String fieldName) {
        if (rootPath instanceof From) {
            From<?, ?> from = (From<?, ?>) rootPath;
            Join<?, ?> join = null;

            for (Join<?, ?> currentJoin : from.getJoins()) {
                if (currentJoin.getAttribute().getName().equals(fieldName)) {
                    join = currentJoin;
                    break;
                }
            }

            if (join == null) {
                join = from.join(fieldName, JoinType.INNER);
            }

            return join;
        }

        throw new IllegalArgumentException("Invalid field: " + fieldName);
    }

    public Long getTotalCount(ProductSearchCriteria searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Product> root = query.from(Product.class);
        query.select(criteriaBuilder.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (searchCriteria.getKeyword() != null) {
            String keyword = "%" + searchCriteria.getKeyword() + "%";
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    keyword.toLowerCase());
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    keyword.toLowerCase());
            predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
        }

        if (searchCriteria.getFieldCriteria() != null) {
            for (FieldCriterion<?> fieldCriterion : searchCriteria.getFieldCriteria()) {
                Predicate predicate = getPredicateForField(root, criteriaBuilder, fieldCriterion);
                predicates.add(predicate);
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }


    private Object convertStringToFieldType(Class<?> fieldType, String value) {
        if (fieldType.equals(Long.class)) {
            return Long.valueOf(value);
        } else if (fieldType.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.equals(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if (fieldType.equals(String.class)) {
            return value;
        }
        // Add more types as needed
        throw new IllegalArgumentException("Unsupported field type: " + fieldType);
    }





}
