package com.sagatechs.generics.utils;

/**
 * Created by rmpestano on 07/01/17.
 */
@SuppressWarnings("ALL")
public interface Constants {


    String DEFAULT_INDEX_PAGE = "pages/index.xhtml";
    String DEFAULT_LOGIN_PAGE = "login.xhtml";
    String DEFAULT_ERROR_PAGE = "pages/exceptions/500.xhtml";
    String DEFAULT_ACCESS_DENIED_PAGE = "pages/exceptions/403.xhtml";
    String DEFAULT_EXPIRED_PAGE = "pages/exceptions/expired.xhtml";
    String DEFAULT_OPTIMISTIC_PAGE = "pages/exceptions/optimistic.xhtml";
    String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    String DEFAULT_PAGE_FORMAT = "xhtml";

    interface InitialParams {
        String DISABLE_FILTER = "com.github.adminfaces.DISABLE_FILTER";
        String LOGIN_PAGE = "com.github.adminfaces.LOGIN_PAGE";
        String ERROR_PAGE = "com.github.adminfaces.ERROR_PAGE";
        String INDEX_PAGE = "com.github.adminfaces.INDEX_PAGE";
    }
}
