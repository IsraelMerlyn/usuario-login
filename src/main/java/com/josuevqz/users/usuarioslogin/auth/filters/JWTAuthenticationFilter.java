package com.josuevqz.users.usuarioslogin.auth.filters;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josuevqz.users.usuarioslogin.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.josuevqz.users.usuarioslogin.auth.TokenJWTConfig.*;



public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private  AuthenticationManager authenticationManager;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
       this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();

//            logger.info("Username desde request InputStream(raw) " + username);  comentando despues de validar su funcionamiento
//            logger.info("Password desde request InputStream(raw) " + password);

        } catch (StreamReadException | DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

        @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String username = ((org.springframework.security.core.userdetails.User)authResult.getPrincipal()).getUsername();

        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
            boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));//validando si el rol es ROLE_ADMIN
            Claims claims = Jwts.claims();
            claims.put("authorities", new ObjectMapper().writeValueAsString(roles)); //validando la autorizacion de los roles
            claims.put("isAdmin", isAdmin); //validando rol admin
            claims.put("username", username); //validando rol username


        //generacion de token y definicion de limite de tiempo de expiracion del token
        String token = Jwts.builder().setClaims(claims).setSubject(username).signWith(SECRET_KEY).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000)).compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_KEY + token);

            Map<String, Object> body = new HashMap<>();
            body.put("token", token);
            body.put("message", String.format("Hola %s, iniciado con exito!", username));
            body.put("username", username);
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(200);
            response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", MESSAGE_ERROR_AUTENTICATION);
        body.put("error",failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");

    }
}
