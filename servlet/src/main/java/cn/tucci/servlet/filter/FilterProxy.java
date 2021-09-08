package cn.tucci.servlet.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 代理filter，
 * 如果内部有其他filter则会执行内部filter链 {@link ChainProxy}，
 * 没有则执行servlet的filter链
 *
 * @author tucci.lee
 */
public class FilterProxy implements Filter {

    private List<Filter> filters;

    public FilterProxy() {

    }

    public FilterProxy(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter proxy");
        if (filters == null || filters.isEmpty()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        ChainProxy chainProxy = new ChainProxy(filterChain, filters);
        chainProxy.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

}
