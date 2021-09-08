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
    private int size = 0;
    private int index = 0;

    public ChainProxy(FilterChain originalChain, List<Filter> filters) {
        this.originalChain = originalChain;
        if(filters != null && !filters.isEmpty()){
            size = filters.size();
            this.filters = filters;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        if (index == size) {
            originalChain.doFilter(servletRequest, servletResponse);
        }else {
            index++;
            Filter nextFilter = filters.get(index - 1);
            nextFilter.doFilter(servletRequest, servletResponse, this);
        }
    }
}
