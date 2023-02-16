package com.assignment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.assignment.component.JwtAuthenticationEntryPointComponent;
import com.assignment.component.JwtRequestFilterComponent;

@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPointComponent jwtAuthenticationEntryPointComponent;

	@Autowired
	private JwtRequestFilterComponent jwtRequestFilterComponent;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
			// don't authenticate this particular request
			.authorizeRequests().antMatchers("/register", "/login", "/books").permitAll()
			// all other requests need to be authenticated
			.anyRequest().authenticated().and()
			// make sure we use state less session; session won't be used to store user's state.
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPointComponent).and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilterComponent, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}

}
