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

import org.eclipse.core.runtime.Status;
import org.eclipse.rwt.RWT;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CkEditorActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.eclipsesource.widgets.ckedit"; //$NON-NLS-1$

	// The shared instance
	private static CkEditorActivator plugin;

	/**
	 * The constructor
	 */
	public CkEditorActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		RWT.getServiceManager().registerServiceHandler(
				CkEditorServiceHandler.HANDLER_URL,
				new CkEditorServiceHandler());
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		RWT.getServiceManager().unregisterServiceHandler(CkEditorServiceHandler.HANDLER_URL);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CkEditorActivator getDefault() {
		return plugin;
	}

	public void log(int error, String msg) {
		Status status = new Status(error, PLUGIN_ID, msg);
		getLog().log(status);
	}

}
