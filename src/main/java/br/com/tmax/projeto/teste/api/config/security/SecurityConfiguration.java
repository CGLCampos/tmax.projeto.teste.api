//package br.com.tmax.projeto.teste.api.config.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@EnableWebSecurity
//@Configuration
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//	
//	@Autowired	
//	private UserDetailsService autenticacaoService;
//
//	//Conficurações de Autenticação
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
//	}
//	
//	//Configurações de Autorização
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//		.antMatchers(HttpMethod.GET, "/livros").permitAll()
//		.antMatchers(HttpMethod.POST, "/livros").permitAll()
//		.anyRequest().permitAll();
//	}
//	
//	//Configurações de recursos estáticos(js, css, imagens, etc.)
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//	}
//}
//