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

package net.paissad.waqtsalat.service;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
/**
 * This class contains some ANSI Term Colors definitions/values that can be used
 * in order to colorize the output onto the terminal.<br>
 * It possible to have more idea by taking a look a this <a
 * href="http://www.termsys.demon.co.uk/vtansi.htm#colors">link</a>.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface AnsiTermColors {

    /*
     * These are Foreground Colors !
     */
    String BLACK_COLOR  = "\u001b[0;30m";
    String RED_COLOR    = "\u001b[0;31m";
    String GREEN_COLOR  = "\u001b[0;32m";
    String YELLOW_COLOR = "\u001b[0;33m";
    String BLUE_COLOR   = "\u001b[0;34m";
    String PURPLE_COLOR = "\u001b[0;35m";
    String CYAN_COLOR   = "\u001b[0;36m";
    String WHITE_COLOR  = "\u001b[0;37m";

    String BOLD         = "\u001b[1m";

    String RESET_COLOR  = "\u001b[0m";

}
