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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((req) -> req.requestMatchers("/", "/login", "/home").permitAll()
                        .requestMatchers("/bidList","/curvePoint","/rating","/ruleName","/trade").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/home", "/error").permitAll()
                        .requestMatchers("/admin/home").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("ADMIN")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
//                .formLogin(form -> {
//                            try {
//                                form
//                                        .loginPage("/app/login")
//                                        .usernameParameter("username")
//                                        .passwordParameter("password")
//                                        .defaultSuccessUrl("/home", true)
//                                        .failureUrl("/app/login?error")
//                                        .failureHandler(new SimpleUrlAuthenticationFailureHandler("/app/login?error")
//                                        )
//                                        .loginPage("/app/login");
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        })
                //.formLogin(Customizer.withDefaults())
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


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
