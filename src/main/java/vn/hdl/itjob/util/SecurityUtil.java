package vn.hdl.itjob.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.user.RespLoginDTO;

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

    public String createToken(Authentication authentication, User user, boolean isRefresh) {
        RespLoginDTO.UserInsideToken userInsideToken = new RespLoginDTO.UserInsideToken();
        userInsideToken.setId(user.getId());
        userInsideToken.setEmail(user.getEmail());
        userInsideToken.setName(user.getName());

        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

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
            .subject(authentication.getName())
            .claim(IDENTITY_CLAIM, userInsideToken)
            .claim(AUTHORITIES_CLAIM, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }    
}
