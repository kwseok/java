package io.teamscala.java.core.data;

import io.teamscala.java.core.data.Sort.Order;

import javax.validation.constraints.Min;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * Pagination &amp; sort request information class.
 */
public class PageRequest implements Pageable {

    // Fields

    /**
     * 페이지 인덱스
     */
    @Min(0)
    private int index;

    /**
     * 페이지 사이즈
     */
    @Min(0)
    private int size;

    /**
     * 페이지 경계 사이즈
     */
    @Min(0)
    private int boundSize;

    /**
     * 정렬
     */
    private Sort sort;

    // Constructors

    /**
     * 기본 생성자
     */
    public PageRequest() {
        this((Sort) null);
    }

    /**
     * 생성자.
     *
     * @param orders 정렬식 리스트
     */
    public PageRequest(String... orders) {
        this(new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param orders 정렬식 리스트
     */
    public PageRequest(Order... orders) {
        this(new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param orders 정렬식 리스트
     */
    public PageRequest(List<Order> orders) {
        this(new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param sort 정렬
     */
    public PageRequest(Sort sort) {
        this(0, DEFAULT_SIZE, DEFAULT_BOUND_SIZE, sort);
    }

    /**
     * 생성자.
     *
     * @param index 페이지 인덱스
     */
    public PageRequest(int index) {
        this(index, (Sort) null);
    }

    /**
     * 생성자.
     *
     * @param index  페이지 인덱스
     * @param orders 정렬식 리스트
     */
    public PageRequest(int index, String... orders) {
        this(index, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index  페이지 인덱스
     * @param orders 정렬식 리스트
     */
    public PageRequest(int index, Order... orders) {
        this(index, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index  페이지 인덱스
     * @param orders 정렬식 리스트
     */
    public PageRequest(int index, List<Order> orders) {
        this(index, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index 페이지 인덱스
     * @param sort  정렬
     */
    public PageRequest(int index, Sort sort) {
        this(index, DEFAULT_SIZE, DEFAULT_BOUND_SIZE, sort);
    }

    /**
     * 생성자.
     *
     * @param index 페이지 인덱스
     * @param size  페이지 사이즈
     */
    public PageRequest(int index, int size) {
        this(index, size, (Sort) null);
    }

    /**
     * 생성자.
     *
     * @param index  페이지 인덱스
     * @param size   페이지 사이즈
     * @param orders 정렬식 리스트
     */
    public PageRequest(int index, int size, String... orders) {
        this(index, size, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index  페이지 인덱스
     * @param size   페이지 사이즈
     * @param orders 정렬식 리스트
     */
    public PageRequest(int index, int size, Order... orders) {
        this(index, size, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index  페이지 인덱스
     * @param size   페이지 사이즈
     * @param orders 정렬식 리스트
     */
    public PageRequest(int index, int size, List<Order> orders) {
        this(index, size, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index 페이지 인덱스
     * @param size  페이지 사이즈
     * @param sort  정렬
     */
    public PageRequest(int index, int size, Sort sort) {
        this(index, size, DEFAULT_BOUND_SIZE, sort);
    }

    /**
     * 생성자.
     *
     * @param index     페이지 인덱스
     * @param size      페이지 사이즈
     * @param boundSize 페이지 경계 사이즈
     */
    public PageRequest(int index, int size, int boundSize) {
        this(index, size, boundSize, (Sort) null);
    }

    /**
     * 생성자.
     *
     * @param index     페이지 인덱스
     * @param size      페이지 사이즈
     * @param boundSize 페이지 경계 사이즈
     * @param orders    정렬식 리스트
     */
    public PageRequest(int index, int size, int boundSize, String... orders) {
        this(index, size, boundSize, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index     페이지 인덱스
     * @param size      페이지 사이즈
     * @param boundSize 페이지 경계 사이즈
     * @param orders    정렬식 리스트
     */
    public PageRequest(int index, int size, int boundSize, Order... orders) {
        this(index, size, boundSize, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index     페이지 인덱스
     * @param size      페이지 사이즈
     * @param boundSize 페이지 경계 사이즈
     * @param orders    정렬식 리스트
     */
    public PageRequest(int index, int size, int boundSize, List<Order> orders) {
        this(index, size, boundSize, new Sort(orders));
    }

    /**
     * 생성자.
     *
     * @param index     페이지 인덱스
     * @param size      페이지 사이즈
     * @param boundSize 페이지 경계 사이즈
     * @param sort      정렬
     */
    public PageRequest(int index, int size, int boundSize, Sort sort) {
        this.setIndex(index);
        this.setSize(size);
        this.setBoundSize(boundSize);
        this.setSort(sort);
    }

    // Getters and Setters...

    @Override
    public int getIndex() {
        return index;
    }

    /**
     * 페이지 인덱스 넘버를 세팅한다.
     *
     * @param index 페이지 인덱스
     */
    public void setIndex(int index) {
        if (index < 0) throw new IllegalArgumentException("'index' must not be negative");
        this.index = index;
    }

    @Override
    public int getSize() {
        return size;
    }

    /**
     * 페이지 사이즈를 세팅한다.
     *
     * @param size 페이지 사이즈
     */
    public void setSize(int size) {
        if (size < 0) throw new IllegalArgumentException("'size' must not be negative");
        this.size = size;
    }

    @Override
    public int getBoundSize() {
        return boundSize;
    }

    /**
     * 페이지 경계 사이즈를 세팅한다.
     *
     * @param boundSize 페이지 경계 사이즈
     */
    public void setBoundSize(int boundSize) {
        if (boundSize < 0) throw new IllegalArgumentException("'boundSize' must not be negative");
        this.boundSize = boundSize;
    }

    @Override
    public int getOffset() {
        return (index * size);
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    public PageRequest setSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public PageRequest setSort(String... orders) {
        setSort(new Sort(orders));
        return this;
    }

    public PageRequest setSort(Order... orders) {
        setSort(new Sort(orders));
        return this;
    }

    public PageRequest setSort(List<Order> orders) {
        setSort(new Sort(orders));
        return this;
    }

    // Override Object

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
