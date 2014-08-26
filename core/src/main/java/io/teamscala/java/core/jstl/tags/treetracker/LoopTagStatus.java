package io.teamscala.java.core.jstl.tags.treetracker;

/**
 * Loop tag status.
 *
 */
public interface LoopTagStatus {

    /** @return 현재 아이템 */
    Object getCurrent();

    /** @return 인덱스 */
    int getIndex();

    /** @return 카운트 */
    int getCount();

    /** @return 깊이 */
    int getDepth();

    /** @return 현재 깊이의 인덱스 */
    int getIndexInDepth();

    /** @return 현재 깊이의 카운트 */
    int getCountInDepth();

    /** @return 최근 마지막 깊이 */
    int getLastDepth();

    /** @return 첫번째 여부 */
    boolean isFirst();

    /** @return 마지막 여부 */
    boolean isLast();
}
