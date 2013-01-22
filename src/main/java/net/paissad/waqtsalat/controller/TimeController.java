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

package net.paissad.waqtsalat.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("time")
@ApplicationScoped
public class TimeController implements Controller {

    private static final long serialVersionUID = 1L;

    /**
     * @return The copyright date in String format. Ex: 2011-2012
     */
    public String getCopyrightDate() {
        Calendar cal = Calendar.getInstance(Locale.US);
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        final String startYear = "2011";
        String currentYear = df.format(cal.getTime());
        return (startYear.equals(currentYear)) ? startYear : startYear + "-" + currentYear;
    }

}
