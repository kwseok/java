package io.teamscala.java.core.web.servlet.view;

import io.teamscala.java.core.web.util.WebUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 파일 뷰
 *
 * @author 석기원
 */
public class FileView extends AbstractView {

	private final File file;
	private String filename;

	public FileView(File file) { this(file, null); }
	public FileView(File file, String filename) {
		Assert.notNull(file, "File must not be null");
		Assert.state(file.length() > 0, "File must not be empty");

		this.file = file;
		this.filename = filename != null ? filename : file.getName();
		super.setContentType("application/octet-stream");
	}

	public File getFile() { return file; }

	public String getFilename() { return filename; }
	public void setFilename(String filename) { this.filename = filename; }

	// AbstractView implementations

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WebUtils.download(request, response, file, filename);
	}
}