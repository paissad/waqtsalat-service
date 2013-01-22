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

package net.paissad.waqtsalat.controller.listener;

import java.util.Collection;
import java.util.Enumeration;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author paissad
 * 
 */
public class HeadersPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        Collection<String> headerNames = (Collection<String>) ((HttpServletRequest) response).getHeaderNames();
        StringBuilder sb = new StringBuilder();
        for (String oneHeaderName : headerNames) {
            sb.append(oneHeaderName).append(" : ").append(((HttpServletRequest) response).getHeader(oneHeaderName)).append("\n");
        }
        System.out.println(sb.toString()); // XXX
        facesContext.addMessage(null, new FacesMessage(sb.toString()));
        
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Enumeration<String> requestHeaders = request.getHeaderNames();
        sb = new StringBuilder();
        while (requestHeaders.hasMoreElements()) {
            String oneHeaderName = requestHeaders.nextElement();
            sb.append(oneHeaderName).append(" : ").append(request.getHeader(oneHeaderName)).append("\n\n");
        }
        System.out.println(sb.toString()); // XXX
        facesContext.addMessage(null, new FacesMessage(sb.toString()));
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
