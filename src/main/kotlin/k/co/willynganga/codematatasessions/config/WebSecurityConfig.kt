package k.co.willynganga.codematatasessions.config

import k.co.willynganga.codematatasessions.security.CustomOAuth2User
import k.co.willynganga.codematatasessions.service.CustomOAuth2UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
open class WebSecurityConfig constructor(@Autowired private val customOAuth2UserService: CustomOAuth2UserService) : WebSecurityConfigurerAdapter() {

    private val logger = LoggerFactory.getLogger(WebSecurityConfig::class.java)

    override fun configure(http: HttpSecurity?) {
        http!!
            .authorizeRequests()
            .antMatchers("/login", "/oauth/**", "/oauth2/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .disable()
            .httpBasic()
            .disable()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler { request, response, authentication ->
                val oauthUser = authentication!!.principal as CustomOAuth2User
                logger.info(oauthUser.name)
                logger.info(oauthUser.attributes.toString())
                logger.info(oauthUser.authorities.toString())
                response.sendRedirect("http://localhost:3000/")
            }
    }
}