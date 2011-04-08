/*******************************************************************************
 * Copyright (c) 2002, 2011 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rap.widget.ckedit;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.widget.ckedit.internals.CkEditorServiceHandler;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;


public class CkEditor extends Composite {

  private static final String URL = "/resources/ckeditor.html";
  private final static String SAVE_FUNCTION = "download";
  private boolean loaded;
  private Browser browser;
  private CkEditorServiceHandler serviceHandler;

  public CkEditor( Composite parent, int style ) {
    super( parent, style );
    super.setLayout( new FillLayout() );
    registerServiceHandler();
    browser = new Browser( this, SWT.NONE );
    browser.setUrl( URL );
    browser.addProgressListener( new ProgressListener() {

      public void completed( ProgressEvent event ) {
        loaded = true;
        createBrowserFunctions();
      }

      public void changed( ProgressEvent event ) {
        // not needed
      }
    } );
  }

  @Override
  public void dispose() {
    deregisterServiceHandler();
    super.dispose();
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( "Cannot change internal layout of CkEditor" );
  }

  /**
   * Reads editor's current input and provides it as download.
   */
  public void save() {
    if( loaded ) {
      browser.evaluate( "readContent();" );
    } else {
      MessageDialog.openWarning( getShell(),
                                 "Still Loading",
                                 "Save not possible. Still loading." );
    }
  }

  private void createBrowserFunctions() {
    new BrowserFunction( browser, SAVE_FUNCTION ) {

      @Override
      public Object function( Object[] arguments ) {
        String editorContent = ( String )arguments[ 0 ];
        serviceHandler.setDownloadContent( editorContent );
        String downloadUrl = createDownloadUrl();
        provideDownload( downloadUrl );
        return null;
      }

      /**
       * An invisible browser widget is used to provide the download.
       * 
       * @param downloadUrl
       */
      private void provideDownload( String downloadUrl ) {
        Browser downloadBrowser = new Browser( CkEditor.this, SWT.NONE );
        downloadBrowser.setBounds( 0, 0, 0, 0 );
        downloadBrowser.setUrl( downloadUrl );
      }
    };
  }

  private String createDownloadUrl() {
    StringBuilder url = new StringBuilder();
    url.append( RWT.getRequest().getContextPath() );
    url.append( RWT.getRequest().getServletPath() );
    url.append( "?" );
    url.append( IServiceHandler.REQUEST_PARAM );
    url.append( "=" + serviceHandler.getId() );
    String encodedURL = RWT.getResponse().encodeURL( url.toString() );
    return encodedURL;
  }

  private void registerServiceHandler() {
    serviceHandler = new CkEditorServiceHandler();
    RWT.getServiceManager().registerServiceHandler( serviceHandler.getId(),
                                                    serviceHandler );
  }

  private void deregisterServiceHandler() {
    RWT.getServiceManager().unregisterServiceHandler( serviceHandler.getId() );
    serviceHandler = null;
  }
}
