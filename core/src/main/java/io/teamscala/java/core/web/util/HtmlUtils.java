package io.teamscala.java.core.web.util;

import io.teamscala.java.core.util.StringUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML 유틸리티
 * 
 * @author 석기원
 */
public abstract class HtmlUtils extends org.springframework.web.util.HtmlUtils {

	/**
	 * 태그 패턴
	 */
	public static final Pattern PATTERN_TAG = Pattern.compile("<([a-z]+)(.*?)>");

	
	/**
	 * 공백 및 개행문자는 HTML 문자로 변환한다.
	 * 
	 * @param input 입력 문자열
	 * @return 변환된 문자열
	 */
	public static String nl2br(String input) {
		if (input == null) return null;
		return StringUtils.replace(input, new String[][]{
                { " ", "&nbsp;" },
                { "\n", "<br/>" }
        });
	}

	/**
	 * '&amp;nbsp;' 및 '&lt;br/&gt;' 문자를 공뱅 및 개행문자로 변경한다.
	 * 
	 * @param input 입력 문자열
	 * @return 변환된 문자열
	 */
	public static String br2nl(String input) {
		if (input == null) return null;
		return StringUtils.replace(input, new String[][]{
				{ "&nbsp;", " " },
				{ "<br/>", "\n" }
        });
	}

	/**
	 * HTML 태그를 이스케이프하고 공백 및 개행문자는 HTML 문자로 변환한다.
	 * 
	 * @param input 입력 문자열
	 * @return 변환된 문자열
	 * @see HtmlUtils#htmlEscape(String)
	 */
	public static String nl2brAndEscape(String input) { return nl2br(htmlEscape(input)); }

	/**
	 * HTML 태그를 언이스케이프하고 '&amp;nbsp;' 및 '&lt;br/&gt;' 문자를 공뱅 및 개행문자로 변경한다.
	 * 
	 * @param input
	 *			입력 문자열
	 * @return 변환된 문자열
	 * @see HtmlUtils#htmlUnescape(String)
	 */
	public static String br2nlAndUnescape(String input) { return htmlUnescape(br2nl(input)); }

	/**
	 * XSS 를 방지 한다.
	 * 
	 * @param html HTML문자열
	 * @return XSS 파싱 및 XSS 방지
	 */
	public static String xssClean(String html) {
		if (html == null) return null;

		html = html.replaceAll("(?i)<script(.*?)</script>", "");
		html = html.replaceAll("(?i)<style(.*?)</style>", "");
		html = html.replaceAll("(?i)<iframe(.*?)</iframe>", "");

		StringBuffer output = new StringBuffer();
		Matcher matcher = PATTERN_TAG.matcher(html);
		while (matcher.find()) {
			String attribute = matcher.group(2)
					.replaceAll("\\$", "\\\\\\$")
					.replaceAll("(?i)(src|href)=(\"|\'?)javascript", "$1=$2#javascript")
					.replaceAll("(?i) on([a-z]+)=", " _on$1");
			matcher.appendReplacement(output, "<$1" + attribute + ">");
		}
		matcher.appendTail(output);

		return output.toString();
	}

	/**
	 * HTML 코드를 정리 한다.
	 * 
	 * @param html the html.
	 * @return 정리된 HTML 코드.
	 */
	public static String htmlClean(String html) {
		if (html == null) return null;

		HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		props.setAllowHtmlInsideAttributes(true);
		// props.setAdvancedXmlEscape(false);
		props.setRecognizeUnicodeChars(false);
		TagNode rootNode = cleaner.clean(html);
		return cleaner.getInnerHtml(rootNode.findElementByName("body", true));
	}
}
