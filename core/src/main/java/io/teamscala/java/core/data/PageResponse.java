package io.teamscala.java.core.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Pagination response information class.
 *
 * @param <T> the list item type
 */
public class PageResponse<T> implements Serializable {

    // Fields

    /**
     * 리스트의 시작행 번호
     */
    private long startNo;

    /**
     * 리스트의 시작행 번호(역방향)
     */
    private long reverseNo;

    /**
     * 첫 페이지
     */
    private int firstPage;

    /**
     * 마지막 페이지
     */
    private int lastPage;

    /**
     * 현재 페이지 단위의 시작 페이지
     */
    private int beginPage;

    /**
     * 페이지 단위의 종료 페이지
     */
    private int endPage;

    /**
     * 이전 페이지 경계의 시작 페이지
     */
    private int beginPageOfPrevBound;

    /**
     * 이전 페이지 경계의 종료 페이지
     */
    private int endPageOfPrevBound;

    /**
     * 다음 페이지 경계의 시작 페이지
     */
    private int beginPageOfNextBound;

    /**
     * 다음 페이지 경계의 종료 페이지
     */
    private int endPageOfNextBound;

    /**
     * 조회된 아이템 리스트
     */
    private List<T> list;

    /**
     * 페이지 요청 정보
     */
    private Pageable pageable;

    /**
     * 전체 카운트
     */
    private long totalCount;

    // Constructors

    /**
     * 생성자.
     *
     * @param list       조회된 아이템 리스트
     * @param pageable   페이지 요청 정보
     * @param totalCount 토탈 카운트
     */
    public PageResponse(List<T> list, Pageable pageable, long totalCount) {
        if (list == null)
            throw new IllegalArgumentException("'items' must not be null");

        if (pageable == null)
            throw new IllegalArgumentException("'pageable' must not be null");

        if (totalCount < 0)
            throw new IllegalArgumentException("'totalCount' must not be negative");

        this.list = Collections.unmodifiableList(list);
        this.pageable = pageable;
        this.totalCount = totalCount;

        if (this.totalCount > 0) {
            final int size = this.pageable.getSize();
            final int boundSize = this.pageable.getBoundSize();

            // 시작행 번호.
            this.startNo = 1;
            this.reverseNo = this.totalCount;

            if (size > 0) {
                // 첫 페이지
                this.firstPage = 1;
                // 마지막 페이지.
                this.lastPage = (int) Math.ceil(this.totalCount / (double) size);

                // 페이지 인덱스
                int pageIndex = this.pageable.getIndex();
                // 요청 페이지가 마지막 페이지 보다 클 경우 마지막 페이지를 기준으로 계산한다.
                if (pageIndex > this.lastPage) {
                    pageIndex = this.lastPage > 0 ? this.lastPage - 1 : 0;
                }

                // 경계 인덱스
                int boundIndex = pageIndex / boundSize;

                // 시작행 번호.
                this.startNo += pageIndex * size;
                this.reverseNo -= pageIndex * size;

                // 시작 페이지.
                this.beginPage = boundIndex * boundSize + 1;
                if (this.beginPage > this.lastPage) {
                    this.beginPage = this.lastPage;
                }

                // 종료 페이지
                this.endPage = boundIndex * boundSize + boundSize;
                if (this.endPage > this.lastPage) {
                    this.endPage = this.lastPage;
                }

                if (boundIndex > 0) {
                    // 이전 경계의 시작 페이지
                    this.beginPageOfPrevBound = (boundIndex - 1) * boundSize + 1;
                    // 이전 경계의 종료 페이지
                    this.endPageOfPrevBound = boundIndex * boundSize;
                }

                if (boundIndex < Math.ceil(this.lastPage / boundSize) - 1) {
                    // 다음 경계의 시작 페이지
                    this.beginPageOfNextBound = (boundIndex + 1) * boundSize + 1;
                    // 다음 경계의 종료 페이지
                    this.endPageOfNextBound = (boundIndex + 2) * boundSize;
                    if (this.endPageOfNextBound > this.lastPage) {
                        this.endPageOfNextBound = this.lastPage;
                    }
                }
            }
        }
    }

    // Getters...

    public List<T> getList() {
        return list;
    }

    public int getCurrentPage() {
        return (pageable.getIndex() + 1);
    }

    public int getPageSize() {
        return pageable.getSize();
    }

    public int getPageBoundSize() {
        return pageable.getBoundSize();
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getStartNo() {
        return startNo;
    }

    public long getReverseNo() {
        return reverseNo;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getBeginPageOfPrevBound() {
        return beginPageOfPrevBound;
    }

    public int getEndPageOfPrevBound() {
        return endPageOfPrevBound;
    }

    public int getBeginPageOfNextBound() {
        return beginPageOfNextBound;
    }

    public int getEndPageOfNextBound() {
        return endPageOfNextBound;
    }

    public boolean isNotFirstPage() {
        return (getCurrentPage() != getFirstPage());
    }

    public boolean isNotLastPage() {
        return (getCurrentPage() != getLastPage());
    }

    public boolean isHasPrevBound() {
        return (getBeginPageOfPrevBound() > 0);
    }

    public boolean isHasNextBound() {
        return (getBeginPageOfNextBound() > 0);
    }

    // Override Object

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}