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

package net.paissad.waqtsalat;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Class Messages provides a mechanism to localize the text messages. It is based on {@link ResourceBundle}.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class I18N {

    private static final String         BUNDLE_NAME     = "i18n.messages";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private I18N() {
    }

    /**
     * Returns the locale-specific string associated with the key.
     * 
     * @param key Key for which we want the associated value.
     * @return Descriptive string if key is found or a copy of the key string if it is not.
     */
    public static String getString(final String key) {
        return getString(key, getJSFViewRootLocale());
    }

    public static String getString(final String key, final Locale locale) {
        try {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return "! " + key + " !";
        }
    }

    /**
     * @return The Locale to be used in localizing the response being created for this view.
     */
    public static Locale getJSFViewRootLocale() {
        try {
            return FacesContext.getCurrentInstance().getViewRoot().getLocale();
        } catch (NullPointerException e) {
            return Locale.ENGLISH;
        }
    }

    /**
     * Gets the current ResourceBundle used.
     * 
     * @return The current ResourceBundle which is used.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }

}
