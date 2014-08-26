package io.teamscala.java.core.util;

/**
 * 트리 트레커의 핸들링을 위한 추상클래스.
 *
 * @param <T> 처리할 클래스 타입
 */
public abstract class TreeHandler<T> {

    /**
     * 새로운 깊이로 진입할시에 호출된다. 새로 구현하여 처리하라.
     *
     * @param node 현재 노드의 객체
     * @param index 인덱스
     * @param indexInDepth 깊이의 인덱스
     * @param depth 깊이
     * @param lastDepth 마지막 깊이
     * @throws Exception {@link java.lang.Exception}
     */
    protected void doEnterDepth(T node, int index, int indexInDepth, int depth, int lastDepth) throws Exception {}

    /**
     * 현재 깊이에서 빠져나올때 호출된다. 새로 구현하여 처리하라.
     *
     * @param node 현재 노드의 객체
     * @param index 인덱스
     * @param indexInDepth 깊이의 인덱스
     * @param depth 깊이
     * @param lastDepth 마지막 깊이
     * @throws Exception {@link java.lang.Exception}
     */
    protected void doLeaveDepth(T node, int index, int indexInDepth, int depth, int lastDepth) throws Exception {}

    /**
     * 핸들러 메서드. 새로 구현하여 처리하라.
     *
     * @param node 현재 노드의 객체
     * @param index 인덱스
     * @param indexInDepth 깊이의 인덱스
     * @param depth 깊이
     * @param lastDepth 마지막 깊이
     * @return 리턴값이 'false'이면 작업을 중단한다
     * @throws Exception {@link java.lang.Exception}
     */
    protected abstract boolean doHandle(T node, int index, int indexInDepth, int depth, int lastDepth) throws Exception;
}
