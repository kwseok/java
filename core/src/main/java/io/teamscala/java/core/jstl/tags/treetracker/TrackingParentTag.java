package io.teamscala.java.core.jstl.tags.treetracker;

import io.teamscala.java.core.util.TreeHandler;
import io.teamscala.java.core.util.TreeTracker;

import javax.servlet.jsp.JspException;

/**
 * Object tracing parent tag.
 *
 */
public class TrackingParentTag extends LoopTagSupport {
	private static final long serialVersionUID = 1118282838001547398L;

	public enum Direction { ASC, DESC }

	// 어트리뷰트 값들
	private Direction direction; // 추적시 오름차순 여부

	/** 기본 생성자. */
	public TrackingParentTag() { super(); }

	@Override public void init() {
		super.init();
		this.direction = Direction.ASC;
	}

	@Override public void release() { super.release(); }

	/**
     * @param direction the direction.
     *
	 * 추적시 방향 세팅.
	 */
	public void setDirection(String direction) {
		if (direction == null) throw new IllegalArgumentException("Direction must not be null");
		this.direction = Direction.valueOf(direction.toUpperCase());
	}

	@Override
	protected void prepare(Object root, String property, TreeHandler<Object> handler) throws JspException {
		try {
			new TreeTracker<>(property, null).trackingParent(root, handler, Direction.ASC == direction);
		} catch (Exception e) {
			throw new JspException(e);
		}
	}
}
