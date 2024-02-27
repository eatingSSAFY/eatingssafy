package com.A204.dispatchFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class AddParamHttpRequest extends HttpServletRequestWrapper {
    private HashMap<String, Object> params;

    @SuppressWarnings("unchecked")
    public AddParamHttpRequest(HttpServletRequest request) {
        super(request);
        this.params = new HashMap<String, Object>(request.getParameterMap());
    }

    public String getParameter(String name) {
        String returnValue = null;
        String[] paramArray = getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            returnValue = paramArray[0];
        }
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    public Map getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @SuppressWarnings("unchecked")
    public Enumeration getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    public String[] getParameterValues(String name) {
        String[] result = null;
        String[] tmp = (String[]) params.get(name);
        if (tmp != null) {
            result = new String[tmp.length];
            System.arraycopy(tmp, 0, result, 0, tmp.length);
        }
        return result;
    }

    public void setParameter(String name, String value) {
        String[] oneParam = {value};
        setParameter(name, oneParam);
    }

    public void setParameter(String name, String[] value) {
        params.put(name, value);
    }

}
