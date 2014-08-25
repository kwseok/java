package io.teamscala.java.core.jstl.tags;

import org.apache.taglibs.standard.tag.common.core.Util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * URL 디코드 태그.
 *
 * @author 석기원
 */
public class URLDecodeTag extends BodyTagSupport {
	private static final long serialVersionUID = 1934328646525866963L;

	//--------------------------------------------------------------------------
	// 상수
	//--------------------------------------------------------------------------

	public static final String DEFAULT_ENCODING = "UTF-8";
	
	//--------------------------------------------------------------------------
	// 필드
	//--------------------------------------------------------------------------

	// 어트리뷰트의 값들
	private String var;
	private Object value;
	private String encoding;
	private int scope;

	//--------------------------------------------------------------------------
	// 생성자 및 라이프사이클 관리
	//--------------------------------------------------------------------------

	public URLDecodeTag() { super(); init(); }

	public void init() {
		this.var = null;
		this.value = null;
		this.encoding = null;
		this.scope = PageContext.PAGE_SCOPE;
	}

	@Override public void release() { super.release(); init(); }

	//--------------------------------------------------------------------------
	// Setter methods
	//--------------------------------------------------------------------------

	/** @param var 어트리뷰트를 설정한다. */
	public void setVar(String var) { this.var = var; }

	/** @param value 어트리뷰트를 설정한다. */
	public void setValue(Object value) { this.value = value; }

	/** @param encoding 어트리뷰트를 설정한다. */
	public void setEncoding(String encoding) { this.encoding = encoding; }

	/** @param scope 어트리뷰트를 설정한다. */
	public void setScope(String scope) { this.scope = Util.getScope(scope); }

	//--------------------------------------------------------------------------
	// 태그 핸들
	//--------------------------------------------------------------------------

	@Override
	public int doEndTag() throws JspException {
		String text;
		if (value != null) {
			if (bodyContent != null && bodyContent.getString() != null)
				throw new JspException("Encountered illegal body, given its attributes.");

			text = value.toString();
		} else  if (bodyContent != null && bodyContent.getString() != null) {
			text = bodyContent.getString();
		} else {
			text = "";
		}
		if (encoding == null) {
			encoding = pageContext.getResponse().getCharacterEncoding();
			if (encoding == null) encoding = DEFAULT_ENCODING;
		}
		try {
			String decodedText = URLDecoder.decode(text, encoding);
			if (var == null)
				pageContext.getOut().write(decodedText);
			else
				pageContext.setAttribute(var, decodedText, scope);
		} catch (IOException ex) {
			throw new JspException(ex.toString(), ex);
		}
		return EVAL_PAGE;
	}

}
