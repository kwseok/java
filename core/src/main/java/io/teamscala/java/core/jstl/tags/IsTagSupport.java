package io.teamscala.java.core.jstl.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Is tag support.
 *
 * @author 석기원
 */
public abstract class IsTagSupport extends BodyTagSupport {
	private static final long serialVersionUID = -2517095372997445127L;

	//--------------------------------------------------------------------------
	// 생성자 및 라이프사이클 관리
	//--------------------------------------------------------------------------

	public IsTagSupport() { super(); init(); }

	public void init() {
		
	}

	@Override public void release() { super.release(); init(); }

	//--------------------------------------------------------------------------
	// 태그 핸들
	//--------------------------------------------------------------------------

	@Override public int doStartTag() throws JspException {
		return evaluation() ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}

	//--------------------------------------------------------------------------
	// 하위클래스로 위임할 메서드
	//--------------------------------------------------------------------------

	/**
	 * 이 메서드를 구현하여 처리하도록한다.
     * @return true or false
     * @throws JspException {@link javax.servlet.jsp.JspException}
	 */
	protected abstract boolean evaluation() throws JspException;
}
