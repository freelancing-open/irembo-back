package com.example.iremboback.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jack
 * @version 1.0.0
 */
@Service
public class JWTUtil {

    @Autowired
    private JWTConstant config;

    Date date = new Date();
    Date iat;
    Date expDate;
    Date refTokenExpDate;

    private String jwtToken;

    public String createToken(String user) throws Exception {
        //String jwtID = UUID.randomUUID().toString();
        initDate();
        try {
            //HMAC
            Algorithm algorithmHS = Algorithm.HMAC256(config.getSignatureKey());

            jwtToken = JWT.create().withIssuedAt(iat).withExpiresAt(expDate).withIssuer(config.getIssue())
                    .withAudience(config.getAudience()).withSubject(user)
                    .sign(algorithmHS);

            return jwtToken;

        } catch (JWTCreationException ex) {
            //Invalid Signing configuration / Couldn't convert Claims.
            throw new Exception("JWT Could be created. \n " + ex.getMessage());
        }
    }

    private void initDate() {
        date = new Date();
        iat = new Date(System.currentTimeMillis());
        expDate = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15));
    }

}
