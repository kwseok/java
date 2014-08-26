package io.teamscala.java.core.web.servlet;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * URL pattern matching interceptor adapter.
 *
 */
public abstract class UrlPatternMatchingInterceptorAdapter implements HandlerInterceptor {

	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private PathMatcher pathMatcher = new AntPathMatcher();

	private Set<String> allowedUrlPatterns = new HashSet<>();
	private Set<String> disallowedUrlPatterns = new HashSet<>();

	/**
	 * Set if URL lookup should always use the full path within the current servlet
	 * context. Else, the path within the current servlet mapping is used if applicable
	 * (that is, in the case of a ".../*" servlet mapping in web.xml).
     * @param alwaysUseFullPath true or false. (Default is "false")
	 * @see org.springframework.web.util.UrlPathHelper#setAlwaysUseFullPath
	 */
	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
	}

	/**
	 * Set if context path and request URI should be URL-decoded. Both are returned
	 * <i>undecoded</i> by the Servlet API, in contrast to the servlet path.
	 * Uses either the request encoding or the default encoding according
	 * to the Servlet spec (ISO-8859-1).
     * @param urlDecode true or false.
	 * @see org.springframework.web.util.UrlPathHelper#setUrlDecode
	 */
	public void setUrlDecode(boolean urlDecode) {
		this.urlPathHelper.setUrlDecode(urlDecode);
	}

	/**
	 * Set the UrlPathHelper to use for resolution of lookup paths.
	 * Use this to override the default UrlPathHelper with a custom subclass,
	 * or to share common UrlPathHelper settings across multiple HandlerMappings
	 * and MethodNameResolvers.
     * @param urlPathHelper the url path helper.
	 * @see org.springframework.web.servlet.mvc.multiaction.AbstractUrlMethodNameResolver#setUrlPathHelper
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		Assert.notNull(urlPathHelper, "'urlPathHelper' must not be null");
		this.urlPathHelper = urlPathHelper;
	}

	/**
	 * Set the PathMatcher implementation to use for matching URL paths
	 * against registered URL patterns. Default is AntPathMatcher.
     * @param pathMatcher the path matcher.
	 * @see org.springframework.util.AntPathMatcher
	 */
	public void setPathMatcher(PathMatcher pathMatcher) {
		Assert.notNull(pathMatcher, "'pathMatcher' must not be null");
		this.pathMatcher = pathMatcher;
	}

	/**
	 * 허용 URL 패턴 리스트 세팅.
     * @param allowedUrlPatterns the allowed url patterns.
	 */
	public void setAllowedUrlPatterns(Set<String> allowedUrlPatterns) {
		this.allowedUrlPatterns.addAll(allowedUrlPatterns);
	}

	/**
	 * 제외 URL 패턴 리스트 세팅.
     * @param disallowedUrlPatterns the disallowed url patterns.
	 */
	public void setDisallowedUrlPatterns(Set<String> disallowedUrlPatterns) {
		this.disallowedUrlPatterns.addAll(disallowedUrlPatterns);
	}

	@Override
	public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return !checkUrlPatternsForRequest(request) ||
                preHandleInternal(request, response, handler);

    }

	@Override
	public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
		if (checkUrlPatternsForRequest(request))
			postHandleInternal(request, response, handler, mav);
	}

	@Override
	public final void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (checkUrlPatternsForRequest(request))
			afterCompletionInternal(request, response, handler, ex);
	}

	/**
	 * 이 메서드는 항상 <code>true</code> 를 반환한다. 새로 구현하여 처리하라.
     * @param request the request.
     * @param response the response.
     * @param handler the handler.
     * @return true
     * @throws Exception {@link java.lang.Exception}
	 */
	protected boolean preHandleInternal(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { return true; }

	/**
	 * 이 메서드는 비어있다. 새로 구현하여 처리하라.
     * @param request the request.
     * @param response the response.
     * @param handler the handler.
     * @param mav the model and view.
     * @throws Exception {@link java.lang.Exception}
	 */
	protected void postHandleInternal(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {}

	/**
	 * 이 메서드는 비어있다. 새로 구현하여 처리하라.
     * @param request the request.
     * @param response the response.
     * @param handler the handler.
     * @param ex the exception.
     * @throws Exception {@link java.lang.Exception}
	 */
	protected void afterCompletionInternal(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}


	/**
	 * @return the {@link UrlPathHelper} to use for the resolution of lookup paths.
	 */
	protected UrlPathHelper getUrlPathHelper() { return urlPathHelper; }

	/**
	 * @return the {@link PathMatcher} implementation to use for matching URL paths against registered URL patterns.
	 */
	protected PathMatcher getPathMatcher() { return pathMatcher; }

	/**
	 * @return 허용 URL 패턴 리스트 반환.
	 */
	protected Set<String> getAllowedUrlPatterns() { return allowedUrlPatterns; }

	/**
	 * @return 제외 URL 패턴 리스트 반환.
	 */
	protected Set<String> getDisallowedUrlPatterns() { return disallowedUrlPatterns; }

	/**
     * @param request the request.
	 * @return URL 패턴들을 체크한다.
	 */
	protected boolean checkUrlPatternsForRequest(HttpServletRequest request) {
		final String LOOKUP_PATH = urlPathHelper.getLookupPathForRequest(request);

		if (!disallowedUrlPatterns.isEmpty()) {
			if (disallowedUrlPatterns.contains(LOOKUP_PATH)) return false;

			for (String disallowedUrlPattern : disallowedUrlPatterns) {
				if (getPathMatcher().match(disallowedUrlPattern, LOOKUP_PATH)) return false;
            }
		}

		if (allowedUrlPatterns.isEmpty()) return true;
		if (allowedUrlPatterns.contains(LOOKUP_PATH)) return true;

		for (String allowedUrlPattern : allowedUrlPatterns) {
			if (getPathMatcher().match(allowedUrlPattern, LOOKUP_PATH)) return true;
        }

		return false;
	}

}
