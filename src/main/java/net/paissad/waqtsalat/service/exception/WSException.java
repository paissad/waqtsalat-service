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

package net.paissad.waqtsalat.service.exception;

/**
 * @author paissad
 * 
 */
public class WSException extends Exception {

    private static final long serialVersionUID = 1L;

    public WSException() {
        this("");
    }

    public WSException(String message) {
        super(message);
    }

    public WSException(Throwable cause) {
        super(cause);
    }

    public WSException(String message, Throwable cause) {
        super(message, cause);
    }

}
