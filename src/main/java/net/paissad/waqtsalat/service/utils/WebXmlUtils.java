/*
 * This file is part of WaqtSalat-Service.
 * 
 * WaqtSalat-Service is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WaqtSalat-Service is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WaqtSalat-Service. If not, see <http://www.gnu.org/licenses/>.
 */

package net.paissad.waqtsalat.service.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * @author paissad
 * 
 */
public class WebXmlUtils {

    private WebXmlUtils() {
    }

    /**
     * @param servlet
     * @return All the 'init-param' of the specified servlet.
     */
    public static Map<String, String> getInitParameters(final HttpServlet servlet) {
        Map<String, String> result = new HashMap<String, String>();
        while (servlet.getInitParameterNames().hasMoreElements()) {
            String paramName = servlet.getInitParameter(servlet.getInitParameterNames().nextElement());
            result.put(paramName, servlet.getInitParameter(paramName));
        }
        return result;
    }

    /**
     * @param servlet
     * @return All the 'context-param' of the specified servlet.
     */
    public static Map<String, String> getContextParameters(final HttpServlet servlet) {
        Map<String, String> result = new HashMap<String, String>();
        ServletContext context = servlet.getServletContext();
        Enumeration<String> initParamNames = context.getInitParameterNames();
        while (initParamNames.hasMoreElements()) {
            String initParamName = initParamNames.nextElement();
            String initParamValue = context.getInitParameter(initParamName);
            result.put(initParamName, initParamValue);
        }
        return result;
    }
}
