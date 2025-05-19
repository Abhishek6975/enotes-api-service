package com.koyta.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.koyta.entity.User;
import com.koyta.exception.JwtTokenExpiredException;
import com.koyta.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
	
	private String secretKey ="";
	

	public JwtServiceImpl() {
		try {

			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
		
			SecretKey key = keyGenerator.generateKey();
				
			secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
			
			log.info("JwtServiceImpl initialized successfully.");

		} catch (Exception e) {
			 log.error("JwtServiceImpl initialization failed.", e);
		}

	}

	@Override
	public String generateToken(User user) {
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getRoles());
		claims.put("status", user.getStatus().getIsActive());

		return Jwts.builder()
				.claims().add(claims)
				.subject(user.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 10))
				.and()
				.signWith(getKey())
				.compact();	
	}

	private Key getKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String extractUserName(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}
	
	public String role(String token) {
		Claims claims = extractAllClaims(token);
		return (String) claims.get("role");
	}

	private Claims extractAllClaims(String token) {

		try {
		     return	Jwts.parser()
					.verifyWith(decrytKey(secretKey))
					.build()
					.parseSignedClaims(token)
					.getPayload();
	
		} catch (ExpiredJwtException e) {

			throw new JwtTokenExpiredException("Token is Expired");
		} catch (JwtException e) {

			throw new JwtTokenExpiredException("invalid Jwt token");
		} catch (Exception e) {

			throw e;
		}

	}

	private SecretKey decrytKey(String secretKey) {

		byte[] keyByets = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyByets);
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {

		String userName = extractUserName(token);

		return userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private Boolean isTokenExpired(String token) {

		Claims claims = extractAllClaims(token);
		Date expirationDate = claims.getExpiration();

		// 27 Dec(today) - 28 Dec(Expir)
		return expirationDate.before(new Date());
	}

}
