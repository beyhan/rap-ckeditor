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

import java.util.HashMap;
import java.util.Map;

public class CkEditorInputStore {

	private static Map<String, String> key2inputMap = new HashMap<String, String>();
	
	
	public static void setInput(String key, String input){
		key2inputMap.put(key, input);
	}
	
	public static String getInput(String key){
		return key2inputMap.get(key);
	}
}
