package com.josuevqz.users.usuarioslogin.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import java.io.IOException;
import java.util.*;

import static com.josuevqz.users.usuarioslogin.auth.TokenJWTConfig.*;

public class JWTvalidationFilter extends BasicAuthenticationFilter {
    public JWTvalidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
      String header = request.getHeader(HEADER_AUTHORIZATION);
      if (header == null || !header.startsWith(PREFIX_KEY)){
         chain.doFilter(request, response);
         return;
      }
      String  token = header.replace(PREFIX_KEY,"");
      byte [] tokenDecodeBuyte =  Base64.getDecoder().decode(token);
      String tokenDecode = new String(tokenDecodeBuyte);

      String [] tokenArr = tokenDecode.split(":");

      String secret = tokenArr[0];
      String username = tokenArr[1];

      if (SECRET_KEY.equals(secret)){
          List<GrantedAuthority> authorities = new ArrayList<>();
          authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

          UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(username,null, authorities);

          SecurityContextHolder.getContext().setAuthentication(authentication);
          chain.doFilter(request,response);
      }else {
          Map<String, String> body = new HashMap<>();
          body.put("message",MESSAGE_TOKEN);
          response.getWriter().write(new ObjectMapper().writeValueAsString(body));
          response.setStatus(403);
          response.setContentType("application/json");
      }

    }
}
