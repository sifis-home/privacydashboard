package com.privacydashboard.application.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {


    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    UserDetailsServiceImpl userProvider;

    private UserDetails user;
    private String userNameToken;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (webAuth(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, userProvider.getAuthoritiesByUser(userProvider.loadUser(username)));
        }

        try {
            if (tokenauth(username, password)) {
                return new UsernamePasswordAuthenticationToken(userNameToken, password, userProvider.getAuthoritiesByUser(userProvider.loadUser(userNameToken)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BadCredentialsException("Authentication failed");
    }

    private boolean webAuth(String username, String password) {
        try {
            user = userProvider.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return false;
        }
        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    private boolean tokenauth(String username, String password) {
        User res = null;
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);


        String response = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            response = restTemplate.postForObject(
                    "https://yggio.sifis-home.eu/api/auth/local",
                    params,
                    String.class
            );
            String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAppmHi3xnw7JbWiAiXj8wnFe3S0V1O0YLfvC17De4TvdePiOSsDa3j0egvu/QjXvBHyKbgmMacrD4Y74h30k1OSm5ttqa9kWLkyR6ilmxDnB97VKS183cJmXUIqBs6QiLwqs5Vjrhk6oye/OvYCvNbbJmxcsHtpWynDD/R/erW354GQlSxfMp0tHG+KSrI78/eRssOa51cxRI2ylvEGGcElUyZYM7HBAQwbVNjVOsyodH9GHZCFQzBcNeSsga93TK24nMAF0J7lm76Uf+XYjgzxdcSdoFkZ2YShYW8LPKGgpQF2WDd5brBELnifVOA57hlb2Xj1TeWKSKPajJnRdDtQIDAQAB";
            byte[] publicKeyBytes = Base64.getDecoder().decode(pk);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            JsonNode root = objectMapper.readTree(response);
            String jwtToken = root.get("token").asText();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            userNameToken = claims.get("email", String.class);

            try {
                user = userProvider.loadUserByUsername(userNameToken);
            } catch (UsernameNotFoundException e) {
                User tmp = new User();
                tmp.setHashedPassword("aaaa");
                tmp.setMail(userNameToken);
                tmp.setRole(GlobalVariables.Role.SUBJECT);
                tmp.setName(userNameToken);
                userProvider.registerUser(tmp);
                return true;
            }
            return true;

        } catch (HttpClientErrorException e) {
            System.err.println("401");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean validateJwt(String jwt) {
        // Valida il JWT qui
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
