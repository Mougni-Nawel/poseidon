package com.nnk.springboot.config;

import com.nnk.springboot.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 *
 * Class of configuration of the security of the app
 * @author Mougni
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * This method configures the http filters, authentication and authorization.
     * It defines the filters of the different http requests,
     * the authentication and the authorization.
     * @param http is used to configure the security filters
     * @return the filter chain configured
     * @throws Exception if an error is thrown
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((req) -> req.requestMatchers("/", "/login", "/home").permitAll()
                        .requestMatchers("/bidList","/curvePoint","/rating","/ruleName","/trade").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/home", "/error").permitAll()
                        .requestMatchers("/admin/home").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("ADMIN")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/bidList/list")
                        .failureUrl("/login?error")
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error")))
                .logout((logout) ->
                        logout.logoutUrl("/app-logout")
                                .deleteCookies("remove")
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .permitAll())


                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/403"));
        return http.build();

    }


    /**
     * This authentication provider configure the authentication with the user details service and the encoding password.
     * @return an instance of DaoAuthentication configured
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * This method create an instance of user service details
     * @return an instance of user service details
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    /**
     * This method create an instance of Bcrypt password encoder
     * It used for the encryption of the password
     * @return and instance of Bcrypt password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
