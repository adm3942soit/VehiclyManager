package com.adonis.utils;

import com.vaadin.server.*;

/**
 * Created by oksdud on 08.05.2017.
 */
public class VaadinUtils {
    public static String getIpAddress(){
        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        return webBrowser.getAddress();
    }
    public static Page getPage(){
        return Page.getCurrent();
    }
    public static WrappedSession getSession(){
        return VaadinSession.getCurrent().getSession();
    }
}
