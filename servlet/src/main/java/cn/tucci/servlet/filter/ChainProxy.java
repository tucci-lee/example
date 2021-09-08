package cn.tucci.servlet.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 自定义filter链
 *
 * @author tucci.lee
 */
public class ChainProxy implements FilterChain {

    private FilterChain originalChain;
    private List<Filter> filters;
    private int index = 0;

    public ChainProxy(FilterChain originalChain, List<Filter> filters) {
        if (originalChain == null) {
            throw new NullPointerException("original FilterChain cannot be null");
        }
        this.originalChain = originalChain;
        this.filters = filters;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        if (filters == null || index == filters.size()) {
            originalChain.doFilter(servletRequest, servletResponse);
        } else {
            Filter nextFilter = filters.get(index++);
            nextFilter.doFilter(servletRequest, servletResponse, this);
        }
    }
}
