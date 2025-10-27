package net.nicoleroy.dirf.springconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author nckroy
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages= "net.nicoleroy.dirf.controller")
@PropertySources(value = {@PropertySource("classpath:/application.properties")})
@Import(ThymeleafConfig.class)
public class WebConfig implements WebMvcConfigurer {
		
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
}
