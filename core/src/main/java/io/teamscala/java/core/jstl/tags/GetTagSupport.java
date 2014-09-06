package io.teamscala.java.core.jstl.tags;

import org.apache.taglibs.standard.tag.common.core.Util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Get tag support.
 */
public abstract class GetTagSupport extends BodyTagSupport {

    //--------------------------------------------------------------------------
    // 필드
    //--------------------------------------------------------------------------

    // 어트리뷰트의 값들
    private String var;
    private int scope;
    private boolean ignoreNull;

    //--------------------------------------------------------------------------
    // 생성자 및 라이프사이클 관리
    //--------------------------------------------------------------------------

    public GetTagSupport() {
        super();
        init();
    }

    public void init() {
        this.var = null;
        this.scope = PageContext.PAGE_SCOPE;
        this.ignoreNull = false;
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    //--------------------------------------------------------------------------
    // Setter methods
    //--------------------------------------------------------------------------

    /**
     * @param var 어트리뷰트 세팅.
     */
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * @param scope 어트리뷰트 세팅.
     */
    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }

    /**
     * @param ignoreNull 어트리뷰트 세팅.
     */
    public void setIgnoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
    }

    //--------------------------------------------------------------------------
    // 태그 핸들
    //--------------------------------------------------------------------------

    @Override
    public int doStartTag() throws JspException {
        Object value = getObject();
        if (value == null && ignoreNull) return SKIP_BODY;
        pageContext.setAttribute(this.var, value, this.scope);
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException {
        pageContext.removeAttribute(this.var, this.scope);
        return super.doAfterBody();
    }

    //--------------------------------------------------------------------------
    // 하위클래스로 위임할 메서드
    //--------------------------------------------------------------------------

    /**
     * @return 이 메서드를 구현하여 객체를 반환하도록한다.
     */
    protected abstract Object getObject();
}
