package com.josuevqz.users.usuarioslogin.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class TokenJWTConfig {
//    public  final  static String  SECRET_KEY = "algun_token";
    public  final  static  Key SECRET_KEY =  Keys.secretKeyFor(SignatureAlgorithm.HS256);//SecretKey key = Jwts.SIG.HS256.keyBuilder().build();
    public  final  static String  PREFIX_KEY = "Bearer ";
    public  final  static String  HEADER_AUTHORIZATION = "Authorization";
    public  final static  String MESSAGE_TOKEN = "El token no es valido";
    public  final static  String MESSAGE_ERROR_AUTENTICATION = "Error en autenticacion";


}
