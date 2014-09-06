package io.teamscala.java.core.jstl.tags.treetracker;

import io.teamscala.java.core.util.TreeHandler;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 트리 트레커: 루프 태그 지원.
 */
@SuppressWarnings("serial")
public abstract class LoopTagSupport extends TagSupport implements IterationTag, TryCatchFinally {

    //--------------------------------------------------------------------------
    // 내부 클래스
    //--------------------------------------------------------------------------

    // 아이템 랩퍼
    private class ItemWrapper {
        private Object item;
        private int index;
        private int indexInDepth;
        private int depth;
        private int lastDepth;
    }

    //--------------------------------------------------------------------------
    // 필드
    //--------------------------------------------------------------------------

    // 어트리뷰트 값들
    private String var;
    private Object target;
    private String property;
    private String varStatus;

    // 아이템 반복자
    private Iterator<ItemWrapper> items;

    // 상태 변수들
    private ItemWrapper currentItem;    // 현재 아이템
    private LoopTagStatus status;        // LoopTagStatus

    //--------------------------------------------------------------------------
    // 생성자 및 라이프사이클 관리
    //--------------------------------------------------------------------------

    /**
     * 기본 생성자.
     */
    public LoopTagSupport() {
        super();
        init();
    }

    /**
     * 초기화.
     */
    public void init() {
        this.var = null;
        this.target = null;
        this.property = null;
        this.varStatus = null;
        this.currentItem = null;
        this.status = null;
        this.items = null;
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
     * @param target 어트리뷰트 세팅.
     */
    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * @param property 어트리뷰트 세팅.
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @param varStatus 어트리뷰트 세팅.
     */
    public void setVarStatus(String varStatus) {
        this.varStatus = varStatus;
    }

    //--------------------------------------------------------------------------
    // 태그 핸들
    //--------------------------------------------------------------------------

    @Override
    public int doStartTag() throws JspException {
        if (this.target == null) return SKIP_BODY;

        prepare();

        if (!items.hasNext()) return SKIP_BODY;

        currentItem = items.next();

        // 몸체에서 사용할 변수들 준비.
        exposeVariables();

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException {
        if (!items.hasNext()) return SKIP_BODY;

        currentItem = items.next();

        // 몸체에서 사용할 변수들 다시준비.
        exposeVariables();

        return EVAL_BODY_AGAIN;
    }

    @Override
    public void doCatch(Throwable t) throws Throwable {
        throw t;
    }

    @Override
    public void doFinally() {
        unExposeVariables();
    }

    //--------------------------------------------------------------------------
    // 기타 유틸리티 메서드
    //--------------------------------------------------------------------------

    /**
     * 이 메서드를 구현하여 반복자 준비.
     *
     * @param target   기준 객체
     * @param property 프로퍼티
     * @param handler  핸들러 객체
     * @throws JspException {@link javax.servlet.jsp.JspException}
     */
    protected abstract void prepare(Object target, String property, TreeHandler<Object> handler) throws JspException;

    /**
     * 반복자 준비.
     */
    private void prepare() throws JspException {
        final List<ItemWrapper> itemList = new ArrayList<>();
        prepare(target, property, new TreeHandler<Object>() {
            @Override
            protected boolean doHandle(Object node, int index, int indexInDepth, int depth, int lastDepth) {
                ItemWrapper itemWrapper = new ItemWrapper();
                itemWrapper.item = node;
                itemWrapper.index = index;
                itemWrapper.indexInDepth = indexInDepth;
                itemWrapper.depth = depth;
                itemWrapper.lastDepth = lastDepth;
                itemList.add(itemWrapper);
                return true;
            }
        });
        this.items = itemList.iterator();
    }

    /**
     * @return 현재 아이템 반환
     */
    private Object getCurrent() {
        return currentItem.item;
    }

    /**
     * @return LoopTagStatus 객체 반환.
     */
    private LoopTagStatus getLoopStatus() {
        class Status implements LoopTagStatus {
            @Override
            public Object getCurrent() {
                return LoopTagSupport.this.currentItem.item;
            }

            @Override
            public int getIndex() {
                return LoopTagSupport.this.currentItem.index;
            }

            @Override
            public int getCount() {
                return LoopTagSupport.this.currentItem.index + 1;
            }

            @Override
            public int getDepth() {
                return LoopTagSupport.this.currentItem.depth;
            }

            @Override
            public int getIndexInDepth() {
                return LoopTagSupport.this.currentItem.indexInDepth;
            }

            @Override
            public int getCountInDepth() {
                return LoopTagSupport.this.currentItem.indexInDepth + 1;
            }

            @Override
            public int getLastDepth() {
                return LoopTagSupport.this.currentItem.lastDepth;
            }

            @Override
            public boolean isFirst() {
                return (LoopTagSupport.this.currentItem.index == 0);
            }

            @Override
            public boolean isLast() {
                return !LoopTagSupport.this.items.hasNext();
            }
        }
        if (this.status == null) {
            this.status = new Status();
        }
        return this.status;
    }

    /**
     * 사용할 어트리뷰트들 세팅.
     */
    private void exposeVariables() throws JspTagException {
        if (this.var != null) {
            Object current = getCurrent();
            if (current != null)
                pageContext.setAttribute(this.var, current);
            else
                pageContext.removeAttribute(this.var, PageContext.PAGE_SCOPE);
        }
        if (this.varStatus != null) {
            LoopTagStatus loopStatus = getLoopStatus();
            if (loopStatus != null)
                pageContext.setAttribute(this.varStatus, loopStatus);
            else
                pageContext.removeAttribute(this.varStatus, PageContext.PAGE_SCOPE);
        }
    }

    /**
     * 사용된 어트리뷰트들 제거.
     */
    private void unExposeVariables() {
        if (this.var != null)
            pageContext.removeAttribute(this.var, PageContext.PAGE_SCOPE);

        if (this.varStatus != null)
            pageContext.removeAttribute(this.varStatus, PageContext.PAGE_SCOPE);
    }
}
