package io.teamscala.java.core.data;

import com.google.common.base.Objects;
import io.teamscala.java.core.data.Sort.Order;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;
import static org.springframework.util.StringUtils.hasText;

/**
 * Sort information class.
 *
 * @author 석기원
 */
public class Sort implements Iterable<Order>, Serializable {
    private static final long serialVersionUID = 2983416859639177363L;

    // Constants

    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    // Fields

    private List<Order> orders;

    // Constructors

    public Sort(String... orders) {
        if (orders == null || orders.length == 0)
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");

        this.orders = new ArrayList<>(orders.length);
        for (String orderBy : orders) {
            if (orderBy == null)
                throw new IllegalArgumentException("Order is null");

            orderBy = orderBy.trim();
            if (orderBy.length() == 0)
                throw new IllegalArgumentException("Order is empty");

            StringTokenizer orderByToken = new StringTokenizer(orderBy, ",");
            while (orderByToken.hasMoreTokens()) {
                Direction direction;
                String property;

                String order = orderByToken.nextToken().trim();
                int pos = order.lastIndexOf(' ');

                if (pos == -1) {
                    direction = DEFAULT_DIRECTION;
                    property = order;
                } else {
                    direction = Direction.fromString(order.substring(pos + 1));
                    property = order.substring(0, pos).trim();
                }

                this.orders.add(new Order(direction, property));
            }
        }
    }

    public Sort(Order... orders) { this(Arrays.asList(orders)); }

    /**
     * Creates a new {@link Sort} instance.
     *
     * @param orders must not be {@literal null} or contain {@literal null} or empty strings
     */
    public Sort(List<Order> orders) {
        if (null == orders || orders.isEmpty())
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");

        this.orders = orders;
    }

    /**
     * Creates a new {@link Sort} instance.
     *
     * @param direction    defaults to {@link Sort#DEFAULT_DIRECTION} (for {@literal null} cases, too)
     * @param properties must not be {@literal null} or contain {@literal null} or empty strings
     */
    public Sort(Direction direction, String... properties) { this(direction, Arrays.asList(properties)); }

    /**
     * Creates a new {@link Sort} instance.
     *
     * @param direction the direction.
     * @param properties the properties.
     */
    public Sort(Direction direction, List<String> properties) {
        if (properties == null || properties.isEmpty())
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");

        this.orders = new ArrayList<>(properties.size());
        orders.addAll(properties.stream()
                .map(property -> new Order(direction, property))
                .collect(Collectors.toList()));
    }

    // Operation methods

    /**
     * @return Return the reverse sort.
     */
    public Sort reverse() {
        List<Order> reversedOrders = new ArrayList<>(this.orders.size());
        reversedOrders.addAll(this.orders.stream()
                .map(order -> order.with(order.isAscending() ? Direction.DESC : Direction.ASC))
                .collect(Collectors.toList()));

        return new Sort(reversedOrders);
    }

    /** @return To sql string. */
    public String toSqlString() { return toSqlString(null); }

    /**
     * @param alias the alias.
     * @return To sql string.
     */
    public String toSqlString(String alias) {
        StringBuilder sql = new StringBuilder();
        for (Order order : this.orders) {
            if (sql.length() > 0) sql.append(",");

            if (hasText(alias)) sql.append(alias).append(".");
            sql.append(order.getProperty());
            if (Direction.DESC == order.getDirection()) sql.append(" desc");
        }
        return sql.toString();
    }

    // Implementations for java.lang.Iterable

    @Override public Iterator<Order> iterator() { return this.orders.iterator(); }

    // Override of java.lang.Object

    @Override public int hashCode() { return Objects.hashCode(orders); }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Sort)) return false;

        Sort that = (Sort) other;
        return Objects.equal(this.orders, that.orders);
    }

    @Override public String toString() { return collectionToCommaDelimitedString(orders); }

    // Enums

    public enum Direction {
        ASC, DESC;

        public static Direction fromString(String value) {
            try {
                return Direction.valueOf(value.toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        String.format(
                                "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).",
                                value), e);
            }
        }
    }

    // Static inner classes

    public static class Order {

        private final Direction direction;
        private final String property;

        public static Order asc(String property) { return new Order(Direction.ASC, property); }
        public static Order desc(String property) { return new Order(Direction.DESC, property); }

        public Order(String property) { this(DEFAULT_DIRECTION, property); }
        public Order(Direction direction, String property) {
            if (property == null || "".equals(property.trim()))
                throw new IllegalArgumentException("Property must not null or empty!");

            this.direction = direction == null ? DEFAULT_DIRECTION : direction;
            this.property = property;
        }


        public Direction getDirection() { return direction; }

        public String getProperty() { return property; }

        public boolean isAscending() { return this.direction.equals(Direction.ASC); }

        public Order with(Direction direction) { return new Order(direction, this.property); }

        public Sort withProperties(String... properties) { return new Sort(this.direction, properties); }

        @Override public int hashCode() { return Objects.hashCode(direction, property); }
        @Override public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Order)) return false;

            Order that = (Order) other;
            return Objects.equal(this.direction, that.direction)
                    && Objects.equal(this.property, that.property);
        }
        @Override public String toString() { return String.format("%s: %s", property, direction); }
    }
}
