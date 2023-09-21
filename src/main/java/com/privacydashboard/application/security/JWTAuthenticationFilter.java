package com.privacydashboard.application.security;

import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JWTAuthenticationFilter extends OncePerRequestFilter {


    @Autowired UserDetailsServiceImpl userProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAppmHi3xnw7JbWiAiXj8wnFe3S0V1O0YLfvC17De4TvdePiOSsDa3j0egvu/QjXvBHyKbgmMacrD4Y74h30k1OSm5ttqa9kWLkyR6ilmxDnB97VKS183cJmXUIqBs6QiLwqs5Vjrhk6oye/OvYCvNbbJmxcsHtpWynDD/R/erW354GQlSxfMp0tHG+KSrI78/eRssOa51cxRI2ylvEGGcElUyZYM7HBAQwbVNjVOsyodH9GHZCFQzBcNeSsga93TK24nMAF0J7lm76Uf+XYjgzxdcSdoFkZ2YShYW8LPKGgpQF2WDd5brBELnifVOA57hlb2Xj1TeWKSKPajJnRdDtQIDAQAB";
            String jwtToken = getJwtFromRequest(request);
            byte[] publicKeyBytes = Base64.getDecoder().decode(pk);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            String userID;
            if (jwtToken != null ) {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(publicKey)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();
                userID = claims.get("email", String.class);

                User user = null;

                try {
                    user = userProvider.loadUser(userID);
                } catch (UsernameNotFoundException e) {
                    user = new User();
                    user.setMail(userID);
                    user.setRole(GlobalVariables.Role.SUBJECT);
                    user.setName(userID);
                    userProvider.registerUser(user);
                }


                Authentication auth = new UsernamePasswordAuthenticationToken(userID, null, userProvider.getAuthoritiesByUser(userProvider.loadUser(userID)));
                SecurityContextHolder.getContext().setAuthentication(auth);

                Authentication authCheck = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Is Authenticated after set: " + authCheck.isAuthenticated());
            }
        } catch (Exception ex) {
            // In caso di fallimento dell'autenticazione, pulisci il contesto di sicurezza
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}

