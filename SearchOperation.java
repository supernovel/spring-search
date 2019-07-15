package com.pixup.admin.common.search;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS, GREATER_THAN_EQUAL, LESS_THAN_EQUAL;

    public static final String[] OPERATION_SET = { ">=", "<=", ":", "!", ">", "<", "~" };

    public static final String[] FLAG_SET = { "'", "*" };

    public static final String OR_PREDICATE_FLAG = "'";

    public static final String ZERO_OR_MORE_REGEX = "*";

    public static final String OR_OPERATOR = "OR";

    public static final String AND_OPERATOR = "AND";

    public static final String LEFT_PARANTHESIS = "(";

    public static final String RIGHT_PARANTHESIS = ")";

    public static SearchOperation getSimpleOperation(final String input) {
        switch (input) {
            case ":":
                return EQUALITY;
            case "!":
                return NEGATION;
            case ">":
                return GREATER_THAN;
            case ">=":
                return GREATER_THAN_EQUAL;
            case "<":
                return LESS_THAN;
            case "<=":
                return LESS_THAN_EQUAL;
            case "~":
                return LIKE;
            default:
                return null;
        }
    }
}