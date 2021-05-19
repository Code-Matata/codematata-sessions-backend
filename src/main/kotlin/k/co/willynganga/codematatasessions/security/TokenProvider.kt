package k.co.willynganga.codematatasessions.security

import io.jsonwebtoken.*
import k.co.willynganga.codematatasessions.config.AppProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenProvider constructor(@Autowired private val appProperties: AppProperties) {

    private val logger = LoggerFactory.getLogger(TokenProvider::class.java)

    fun createToken(authentication: Authentication): String {
        val principal = authentication.principal as CustomOAuth2User

        val now = Date()
        val expiryDate = Date(now.time + appProperties.getAuth().tokenExpirationMsec)

        return Jwts.builder()
            .setSubject(principal.name)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().tokenSecret)
            .compact()
    }

    fun getUserIdFromToken(token: String): String {
        val claims = Jwts.parser()
            .setSigningKey(appProperties.getAuth().tokenSecret)
            .parseClaimsJws(token)
            .body

        return claims.subject.toString()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().tokenSecret).parseClaimsJws(token)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty")
        }

        return false
    }
}