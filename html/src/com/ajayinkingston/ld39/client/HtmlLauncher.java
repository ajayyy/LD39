package com.ajayinkingston.ld39.client;

import com.ajayinkingston.ld39.Main;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HtmlLauncher extends GwtApplication {
	
	static Main main;
	
	 @Override
     public GwtApplicationConfiguration getConfig () {
     	GwtApplicationConfiguration config = new GwtApplicationConfiguration(getWindowInnerWidth(), getWindowInnerHeight());
     	config.preferFlash = false;
     	
     	 Element element = Document.get().getElementById("embed-html");
          VerticalPanel panel = new VerticalPanel();
          panel.setWidth("100%");
          panel.setHeight("100%");
          panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
          panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
          element.appendChild(panel.getElement());
          config.rootPanel = panel;
          
//          tempWorkaroundForIssue4590();
          
         return config;
     }

     @Override
     public ApplicationListener createApplicationListener () {
     	setLoadingListener(new LoadingListener() {
             @Override
             public void beforeSetup() {

             }

             @Override
             public void afterSetup() {
                 setupResizeHook();
             }
         });
     	main = new Main();
         return main;
     }
     
     @Override
     public void onModuleLoad() {
     	super.onModuleLoad();
//         tempWorkaroundForIssue4590();
     }
     
     native static int getWindowInnerWidth()/*-{
     	return $wnd.innerWidth;
 	}-*/;

     native static int getWindowInnerHeight()/*-{
     	return $wnd.innerHeight;
 	}-*/;
     
     native void setupResizeHook() /*-{
	        var htmlLauncher_onWindowResize = $entry(@com.ajayinkingston.ld39.client.HtmlLauncher::handleResize());
	        $wnd.addEventListener('resize', htmlLauncher_onWindowResize, false);
 	}-*/;
     
     public static void handleResize() {
         Element element = Document.get().getElementById("embed-html");
         NodeList<Element> nl = element.getElementsByTagName("canvas");
         Element canvas = nl.getItem(0);

     	canvas.getStyle().setWidth(getWindowInnerWidth(), Style.Unit.PX);
         canvas.getStyle().setHeight(getWindowInnerHeight(), Style.Unit.PX);
         
         Gdx.graphics.setWindowedMode(getWindowInnerWidth(),getWindowInnerHeight());
         Gdx.gl.glViewport(0, 0, getWindowInnerWidth(), getWindowInnerHeight());
//         Window.scrollTo((cfg.width - width)/2,(cfg.height - height)/2);
         main.resize(getWindowInnerWidth(), getWindowInnerHeight());
     }
}