package io.teamscala.java.core.data;

/**
 * Pageable interface.
 *
 */
public interface Pageable {

    /** 기본 페이지 사이즈. */
    int DEFAULT_SIZE = 10;

    /** 기본 페이지 경계 사이즈. */
    int DEFAULT_BOUND_SIZE = 10;

    /**
     * @return 페이지 인덱스를 반환한다. (0 부터 시작한다)
     */
    default int getIndex() { return 0; }

    /**
     * @return 페이지 사이즈를 반환한다. (기본값 {@value #DEFAULT_SIZE})
     */
    default int getSize() { return DEFAULT_SIZE; }

    /**
     * @return 페이지 경계 사이즈를 반환한다. (기본값 {@value #DEFAULT_BOUND_SIZE})
     */
    default int getBoundSize() { return DEFAULT_BOUND_SIZE; }

    /**
     * @return 페이지 오프셋 값을 반환한다.
     */
    default int getOffset() { return 0; }

    /**
     * @return 정렬 파라미터를 반환한다.
     */
    default Sort getSort() { return null; }
}
