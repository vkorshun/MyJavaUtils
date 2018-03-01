package com.wgsoftpro.configuration;

import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by vkorshun on 18.02.2017.
 */
public class UniversalWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[]{};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] {UniversalWebConfigurationContext.class, WebConfig.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  /*@Override
  protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    registration.setInitParameter("dispatchOptionsRequest", "true");
  }*/
  @Override
  protected Filter[] getServletFilters() {
    Filter[] singleton = {new CORSFilter()};
    return singleton;
  }

   @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);
    servletContext.addListener(new RequestContextListener());
  }

}
