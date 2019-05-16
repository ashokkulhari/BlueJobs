package com.bluejob.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException, ServletException {
    	logger.error("Unauthorized error. Message - {}", e.getMessage());
    	
    	if(response.getStatus() == 4013){
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Expired JWT token,code="+response.getStatus());
    	}else if(response.getStatus() == 4014){
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Invalid JWT signature,code="+response.getStatus());
    	}else if(response.getStatus() == 4015){
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Invalid JWT token,code="+response.getStatus());
    	}else if(response.getStatus() == 4016){
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Unsupported JWT token,code="+response.getStatus());
    	}else if(response.getStatus() == 4017){
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> JWT claims string is empty,code="+response.getStatus());
    	}else{
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Unauthorized");
    	}
    }
}