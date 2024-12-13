package vn.hdl.itjob.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.user.RespLoginDTO;
import vn.hdl.itjob.util.exception.InvalidException;

@Service
public class SecurityUtil {
    // algorithm
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    public static final String AUTHORITIES_CLAIM = "auth";
    public static final String IDENTITY_CLAIM = "user";

    @Value("${hdl.jwt.base64-secret-key}")
    private String jwtKey;

    @Value("${hdl.jwt.access-token-expiration-in-seconds}")
    private long accessTokenExpiration;

    @Value("${hdl.jwt.refresh-token-expiration-in-seconds}")
    private long refreshTokenExpiration;

    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(User user, boolean isRefresh) {
        RespLoginDTO.UserInsideToken userInsideToken = new RespLoginDTO.UserInsideToken();
        userInsideToken.setId(user.getId());
        userInsideToken.setEmail(user.getEmail());
        userInsideToken.setName(user.getName());

        String authorities = user.getRole() != null ? user.getRole().getName() : "NULL";

        Instant now = Instant.now();
        Instant validity;
        if (isRefresh) {
            validity = now.plus(refreshTokenExpiration, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(user.getEmail())
            .claim(IDENTITY_CLAIM, userInsideToken)
            .claim(AUTHORITIES_CLAIM, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }    


    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public Jwt checkValidToken(String token) throws InvalidException {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                                .macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw e;
        }
    };
        
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
    }
}
