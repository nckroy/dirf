package net.nicholasroy.whitepages.springconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author nick
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages= "net.nicholasroy.whitepages.controller")
@PropertySources(value = {@PropertySource("classpath:/application.properties")})
//@Import(ThymeleafConfig.class)
@Import({ThymeleafConfig.class, PersistenceConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {
		
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
}
