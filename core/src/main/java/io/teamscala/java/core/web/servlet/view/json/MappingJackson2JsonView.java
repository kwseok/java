package io.teamscala.java.core.web.servlet.view.json;

import io.teamscala.java.core.web.util.WebUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Extends MappingJackson2JsonView.
 *
 */
public class MappingJackson2JsonView extends org.springframework.web.servlet.view.json.MappingJackson2JsonView {

	/** Default content type for MSIE. */
	public static final String DEFAULT_MSIE_CONTENT_TYPE = "text/plain";

	//Fix for MSIE iframe file upload
	@Override
	public String getContentType() {
		return WebUtils.isAjaxAndMsieAndMultipart()
				? DEFAULT_MSIE_CONTENT_TYPE : super.getContentType();
	}

	@Override
	protected Object filterModel(Map<String, Object> model) {
		Iterator<Map.Entry<String, Object>> iter = model.entrySet().iterator();
		while (iter.hasNext()) {
			if (iter.next().getValue() instanceof Throwable)
                iter.remove();
        }
		return super.filterModel(model);
	}
}
