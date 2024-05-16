package it.uniroma3.vestiti.auth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
			.usersByUsernameQuery("SELECT username, password, 1 as eneabled FROM credentials WHERE username=?");
			
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception{

        httpSecurity
        		.csrf(csfr ->csfr.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/register", "/registrationSuccessful", "/CSS/**", "/images/**", "favicon.ico").permitAll()
                        .anyRequest().authenticated()
                        )
                .formLogin(login -> login
                		.loginPage("/login").permitAll()
                .defaultSuccessUrl("/success", true)
                .failureUrl("/login?error=true")
                )
                .logout(logout -> logout.logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true)
                .permitAll());
		return httpSecurity.build();
	}
	
	
	
	

}
