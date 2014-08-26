package io.teamscala.java.core.web.util;

import io.teamscala.java.core.util.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Web 관련 유틸.
 * 
 */
public abstract class WebUtils {

	public static final String USER_AGENT_HEADER_NAME = "User-Agent";
	public static final String X_REQUESTED_WITH_HEADER_NAME = "X-Requested-With";
	public static final String XML_HTTP_REQUEST_HEADER_VALUE = "XMLHttpRequest";

	/**
	 * HTTP content type header for multipart forms.
	 */
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";

	public static final String[] AJAX_REQUEST_URI_SUFFIXES = { "ajax", "json", "xml" };
	public static final String AJAX_REQUEST_URI_SUFFIXES_PROP_NAME = WebUtils.class.getName() + ".AjaxRequestUriSuffixes";

	/**
	 * @return 현재 Request 객체를 반환한다.
	 */
	public static HttpServletRequest getCurrentRequest() {
		RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
		if (!(ra instanceof ServletRequestAttributes)) {
			throw new IllegalStateException("Current request is not a servlet request");
		}
		return ((ServletRequestAttributes) ra).getRequest();
	}

	/**
	 * @return 현재 Session 객체를 반환한다.
	 */
	public static HttpSession getCurrentSession() { return getCurrentRequest().getSession(); }

	/**
	 * @return 현재 Loacle 객체를 반환한다.
	 */
	public static Locale getCurrentLocale() {
		return RequestContextUtils.getLocale(getCurrentRequest());
	}

	/**
	 * @return AJAX 요청인지 확인한다.
	 */
	public static boolean isAjax() { return isAjax(getCurrentRequest()); }

	/**
	 * @param request {@link HttpServletRequest}
	 * @return AJAX 요청인지 확인한다.
	 */
	public static boolean isAjax(HttpServletRequest request) {
		if (XML_HTTP_REQUEST_HEADER_VALUE.equals(request.getHeader(X_REQUESTED_WITH_HEADER_NAME))) return true;

		String suffixesString = System.getProperty(AJAX_REQUEST_URI_SUFFIXES_PROP_NAME);
		String[] suffixes;
		if (suffixesString != null) {
			String[] suffixArray = suffixesString.split(",");
			List<String> suffixList = new ArrayList<>(suffixArray.length);
			for (String suffix : suffixArray) {
				String trimmedSuffix = suffix.trim();
				if (trimmedSuffix.length() > 0) suffixList.add(trimmedSuffix);
			}
			if (suffixList.size() == 0) return false;
			suffixes = suffixList.toArray(new String[suffixList.size()]);
		}
		else suffixes = AJAX_REQUEST_URI_SUFFIXES;

		final String lookupPath = request.getRequestURI().toLowerCase();
		if (FilenameUtils.isExtension(lookupPath, suffixes)) return true;

		final String basename = FilenameUtils.getBaseName(lookupPath);
		for (String suffix : suffixes) {
			if (basename.endsWith("_" + suffix)) return true;
		}
		return false;
	}

	/**
	 * @return MSIE 요청인지 확인한다.
	 */
	public static boolean isMsie() { return isMsie(getCurrentRequest()); }

	/**
	 * @param request {@link HttpServletRequest}
	 * @return MSIE 요청인지 확인한다.
	 */
	public static boolean isMsie(HttpServletRequest request) {
		return StringUtils.contains(request.getHeader(USER_AGENT_HEADER_NAME), "MSIE");
	}

	/**
	 * {@value #MULTIPART_FORM_DATA} 요청인지 확인한다.
	 * 
	 * @return Multipart 요청이면 true
	 */
	public static boolean isMultipart() { return isMultipart(getCurrentRequest()); }

	/**
	 * {@value #MULTIPART_FORM_DATA} 요청인지 확인한다.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @return Multipart 요청이면 true
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		if (!"POST".equals(request.getMethod().toUpperCase())) return false;
		String contentType = request.getContentType();
		return contentType != null && contentType.toLowerCase().startsWith(MULTIPART_FORM_DATA);
	}

	/**
	 * AJAX 요청에 MSIE 요청이면서 {@value #MULTIPART_FORM_DATA} 요청인지 확인한다.
	 * 
	 * @return MSIE, Multipart 요청이면서 AJAX 요청이면 true.
	 */
	public static boolean isAjaxAndMsieAndMultipart() {
		return isAjaxAndMsieAndMultipart(getCurrentRequest());
	}

	/**
	 * AJAX 요청에 MSIE 요청이면서 {@value #MULTIPART_FORM_DATA} 요청인지 확인한다.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @return MSIE, Multipart 요청이면서 AJAX 요청이면 true.
	 */
	public static boolean isAjaxAndMsieAndMultipart(HttpServletRequest request) {
		return isAjax(request) && isMsie(request) && isMultipart(request);
	}

	/**
	 * 모바일 브라우저의 요청인지 확인한다.
	 * 
	 * @return 모바일 브라우저 요청이면 true
	 */
	public static boolean isMobile() { return isMobile(getCurrentRequest()); }

	/**
	 * 모바일 브라우저의 요청인지 확인한다.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @return 모바일 브라우저 요청이면 true
	 */
	public static boolean isMobile(HttpServletRequest request) {
		String ua = request.getHeader(USER_AGENT_HEADER_NAME).toLowerCase();
		return ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-");
	}

	/**
	 * 주어진 패스의 파일을 다운로드한다.
	 *
	 * @param request Request
	 * @param response Response
	 * @param path 파일 패스
     * @throws IOException {@link java.io.IOException}
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
		download(request, response, new File(path));
	}

	/**
	 * 주어진 패스의 파일을 다운로드한다.
	 *
	 * @param request Request
	 * @param response Response
	 * @param file 파일
	 * @throws IOException {@link java.io.IOException}
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
		download(request, response, file, null);
	}

	/**
	 * 주어진 패스의 파일을 다운로드한다.
	 *
	 * @param request Request
	 * @param response Response
	 * @param path 파일 패스
	 * @param filename 다운로드 파일이름
     * @throws IOException {@link java.io.IOException}
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String path, String filename) throws IOException {
		download(request, response, new File(path), filename);
	}

	/**
	 * 주어진 패스의 파일을 다운로드한다.
	 *
	 * @param request Request
	 * @param response Response
	 * @param file 파일
	 * @param filename 다운로드 파일이름
     * @throws IOException {@link java.io.IOException}
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, File file, String filename) throws IOException {
		Assert.notNull(request, "'request' must not be null");
		Assert.notNull(response, "'response' must not be null");
		Assert.notNull(file, "'file' must not be null");

		if (filename == null || filename.length() == 0)
			filename = file.getName();

		String encoding = request.getCharacterEncoding();
		if (encoding == null)
			encoding = "UTF-8";

		// 파일이름 인코딩 변환
        if (isMsie(request))
            filename = URLEncoder.encode(filename, encoding);

		response.reset();
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Type", "application/octet-stream; charset=" + encoding);
		setDBCSHeader(response, "Content-Disposition", "attachment;filename=" + filename + ";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");

		FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	/**
	 * DBCS 형태로 인코딩 하여 헤더를 추가한다.
	 * 
	 * @param response {@link HttpServletResponse}
	 * @param header 헤더명
	 * @param value 값
	 */
	public static void setDBCSHeader(HttpServletResponse response, String header, String value) {
		byte b[];
		try {
			b = value.getBytes(response.getCharacterEncoding());
		} catch (UnsupportedEncodingException e) {
			b = value.getBytes();
		}

		char c[] = new char[b.length];
		for (int i = 0; i < b.length; i++) {
			c[i] = (char) (((char)b[i]) & 0xff);
		}

		response.setHeader(header, new String(c));
	}
}
