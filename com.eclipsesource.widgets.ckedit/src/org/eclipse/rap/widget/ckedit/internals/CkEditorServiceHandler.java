/*******************************************************************************
 * Copyright (c) 2002,2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/

package org.eclipse.rap.widget.ckedit.internals;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;

/**
 * A service handler which provides CkEditor input for download. This service
 * handler is registered on bundle start up.
 * 
 */
public class CkEditorServiceHandler implements IServiceHandler {

	public static final String EDITOR_CONTENT_KEY = "contentKey";

	public static final String HANDLER_URL = "downloadHandler";

	public void service() throws IOException, ServletException {
		HttpServletResponse resp = RWT.getResponse();
		sendFile(resp);
	}

	private void sendFile(final HttpServletResponse resp) {
		OutputStream os = null;
		String contentKey = (String) RWT.getRequest().getParameter(EDITOR_CONTENT_KEY);
		try {
			String editorContent = CkEditorInputStore.getInput(contentKey);
			byte[] editorContentBytes = editorContent.getBytes();
			String contentType = "text/html";
			String downloadName = "tmp.html";
			// Set response headers
			resp.setContentType(contentType);
			resp.setContentLength(editorContentBytes.length);
			resp.setHeader("Content-Disposition", "attachment; filename="
					+ "\"" + downloadName + "\"" + ";");
			resp.flushBuffer();
			// Copy documentation to responce's output stream.
			os = resp.getOutputStream();
			os.write(editorContentBytes);
			os.flush();
		} catch (Exception e) {
			CkEditorActivator.getDefault().log(IStatus.ERROR,
					"Download failed. Exception: " + e.getLocalizedMessage());
		} finally {
			//Temporary stored input removed
			CkEditorInputStore.setInput(contentKey, null);
			try {
				os.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}
}
