//package net.nicholasroy.springtest2.springconfig;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
//
//@Configuration
//@EnableWebMvcSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .antMatchers("/resources/**").permitAll()
//                .anyRequest().authenticated();
//        http
//            .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//            .logout()
//                .permitAll();
//
//        //TODO: Figure out how to protect specific URLs for REST services with basic authentication
////        http
////        .authorizeRequests()
////        	.antMatchers("/persistence/**")
////        	.authenticated()
////        	.and()
////        	.httpBasic();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    	//TODO: Figure out how to use the authorities mapper to map group membership to roles
//    	MyAuthoritiesMapper mapper = new MyAuthoritiesMapper();
////    		auth
////    			.ldapAuthentication()
////    				.userDnPatterns("uid={0},DC=EXAMPLE,DC=COM")
////    				.groupSearchBase("DC=OF,DC=SEARCH,DC=BASE,DC=EXAMPLE,DC=COM")
////    				.contextSource()
////    					.ldif("classpath:test-server.ldif");
////    					.url("ldaps://dir.example.com:636/");
////TODO: Why did the above stop working when I upgraded to STS 3.6.0 M1/Java 8/Tomcat 8?
//
//        auth
//            .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
//    }
//}
//
////protected void configure(HttpSecurity http) throws Exception {
////    http
////        .httpBasic()
////            .and()
////        .authorizeRequests()
////            .anyRequest().hasRole("USER");
////}
//
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.core.annotation.Order;
////import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
////import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.builders.WebSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
////
////
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig
////   extends WebSecurityConfigurerAdapter {
////
////  @Autowired
////  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////    auth
////      .inMemoryAuthentication()
////        .withUser("user").password("password").roles("USER");
////  }
////}
//
//
////
////@Configuration
////@EnableWebSecurity
//////@EnableGlobalMethodSecurity(prePostEnabled=true)
////public class SecurityConfig {
////	// Since MultiSecurityConfig does not extend GlobalMethodSecurityConfiguration and
////	// define an AuthenticationManager, it will try using the globally defined
////	// AuthenticationManagerBuilder to create one
////
////	// The @Enable*Security annotations create a global AuthenticationManagerBuilder
////	// that can optionally be used for creating an AuthenticationManager that is shared
////	// The key to using it is to use the @Autowired annotation
////	@Autowired
////	public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
////		auth
////		.inMemoryAuthentication()
////		.withUser("user").password("password").roles("USER").and()
////		.withUser("admin").password("password").roles("USER", "ADMIN");
////	}
////
////	@Configuration
////	@EnableWebSecurity
////	@Order(1)
////	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
////		// Since we didn't specify an AuthenticationManager for this class,
////		// the global instance is used
////
////
////		protected void configure(HttpSecurity http) throws Exception {
////			http
////			//                .antMatcher("/api/**")
////			.antMatcher("/persistence")
////			.httpBasic();
////		}
////	}
////
////	@Configuration
////	@EnableWebSecurity
////	public static class FormWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
////		// Since we didn't specify an AuthenticationManager for this class,
////		// the global instance is used
////
////		public void configure(WebSecurity web) throws Exception {
////			web
////			.ignoring()
////			.antMatchers("/resources/**", "/static/**");
////		}
////
////		protected void configure(HttpSecurity http) throws Exception {
////			http
////			.authorizeRequests()
////			.anyRequest().hasRole("USER")
////			.and()
////			.formLogin()
////			.loginPage("/login.html")
////			.failureUrl("/login-error.html")
////			.permitAll();
////		}
////	}
////
////}