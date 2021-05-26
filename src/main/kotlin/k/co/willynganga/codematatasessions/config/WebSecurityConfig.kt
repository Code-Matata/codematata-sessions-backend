package k.co.willynganga.codematatasessions.config

import k.co.willynganga.codematatasessions.security.RestAuthenticationEntryPoint
import k.co.willynganga.codematatasessions.security.TokenAuthenticationFilter
import k.co.willynganga.codematatasessions.security.oauth2.CustomOAuth2UserService
import k.co.willynganga.codematatasessions.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationFailureHandler
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationSuccessHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
open class WebSecurityConfig constructor(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler
) : WebSecurityConfigurerAdapter() {

    private val logger = LoggerFactory.getLogger(WebSecurityConfig::class.java)

    @Bean
    open fun cookieAuthorizationRequestRepository(): HttpCookieOAuth2AuthorizationRequestRepository {
        return HttpCookieOAuth2AuthorizationRequestRepository()
    }

    @Bean
    fun tokenAuthorizationFilter(): TokenAuthenticationFilter {
     return TokenAuthenticationFilter()
    }

    override fun configure(http: HttpSecurity?) {
        http!!
            .cors()
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .csrf()
                .disable()
            .authorizeRequests()
            .antMatchers("/oauth/**", "/oauth2/**").permitAll()
            .antMatchers("/",
                "/api/v1/instructor/add",
                "/api/v1/student/add")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .disable()
            .httpBasic()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(RestAuthenticationEntryPoint())
                .and()
            .oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                    .and()
                .redirectionEndpoint()
                    .baseUri("/oauth2/callback/*")
                    .and()
                .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)

        http.addFilterBefore(tokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}