package cn.tucci.servlet.test.filter;

import cn.tucci.servlet.filter.FilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tucci.lee
 */
@WebListener
public class RegisterListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // new filter
        UriPrintFilter uriPrintFilter = new UriPrintFilter();
        UrlPrintFilter urlPrintFilter = new UrlPrintFilter();
        List<Filter> filters = Stream.of(uriPrintFilter, urlPrintFilter).collect(Collectors.toList());
        FilterProxy filterProxy = new FilterProxy(filters);

        TestFilter testFilter = new TestFilter();


        // 获取ServletContext
        ServletContext context = servletContextEvent.getServletContext();

        // 注册filter
        context.addFilter("filterProxy", filterProxy)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        context.addFilter("testFilter", testFilter)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
