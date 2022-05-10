package br.com.tmax.projeto.teste.api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.tmax.projeto.teste.api.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired	
	private UserDetailsService autenticacaoService;

	@Autowired	
	private TokenService tokenService; 
	
	@Autowired	
	private UsuarioRepository usuarioRepository; 
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	//Configurações de Autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configurações de Autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		//autorização para livros
		.antMatchers(HttpMethod.POST, "/livros").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/livros/*").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/livros/*").hasRole("ADMIN")
		//autorização para categorias
		.antMatchers(HttpMethod.GET, "/categorias").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/categorias").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/categorias/*").hasRole("ADMIN")
		//autorização para alunos
		.antMatchers(HttpMethod.GET, "/alunos").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/alunos").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/alunos/*").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/alunos/*").hasRole("ADMIN")
		//autorização para reservas
		.antMatchers(HttpMethod.GET, "/reservas").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/reservas/*").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
	}
	
	//Configurações de recursos estáticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
}

