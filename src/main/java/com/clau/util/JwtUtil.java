package com.clau.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.clau.enums.Role;
import com.clau.config.PropertiesConfig;

import java.util.Date;

public class JwtUtil {
  private static String SECRET;
  private static Algorithm ALGORITHM;
  private static long EXPIRATION_TIME;

  private PropertiesConfig propertiesConfig;

  public JwtUtil() {
    this.propertiesConfig = new PropertiesConfig();

    this.SECRET = propertiesConfig.getProperty("jwt.secret");
    this.EXPIRATION_TIME = Long.parseLong(propertiesConfig.getProperty("jwt.expiration_time"));
    this.ALGORITHM = Algorithm.HMAC256(SECRET);
  }

  public String gerarToken(String email, Role role) {
    return JWT.create()
            .withSubject(email)
            .withClaim("role", role.getNome())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(ALGORITHM);
  }

  public String validarToken(String token) {
    return JWT.require(ALGORITHM)
            .build()
            .verify(token)
            .getSubject();
  }

  public String getRoleFromToken(String token) {
    return JWT.require(ALGORITHM)
            .build()
            .verify(token)
            .getClaim("role")
            .asString();
  }

}
