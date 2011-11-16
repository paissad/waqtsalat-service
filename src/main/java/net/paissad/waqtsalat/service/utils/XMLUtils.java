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

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

import net.paissad.waqtsalat.service.WSConstants;

/**
 * @author paissad
 * 
 */
public class XMLUtils {

    private XMLUtils() {
    }

    /**
     * @param document - The document to parse.
     * @param path - The name of the element we are looking for.
     * @param prefix - <b>Note</b>: (optional) The prefix to use, set to
     *            <code>null</code> or empty String in order not to use this
     *            parameter.
     * @return The node having the specified path.
     * @throws IllegalStateException - If no element having the specified name
     *             was found.
     * @throws JDOMException
     */
    public static Object selectNodeHavingPath(
            final Document document,
            final String path,
            final String prefix) throws IllegalStateException, JDOMException {

        XPath xpath = XPath.newInstance(path);
        String prefixToUse = (prefix == null || prefix.trim().isEmpty()) ? "" : prefix;
        xpath.addNamespace(Namespace.getNamespace(prefixToUse, WSConstants.NAMESPACE_CONF));
        Object node = xpath.selectSingleNode(document);
        if (node == null) {
            throw new IllegalStateException("No node having the path '" + path + "' was found.");
        } else {
            return node;
        }
    }

}
