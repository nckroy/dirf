package net.nicholasroy.whitepages.controller;

import javax.persistence.EntityManager;

import net.nicholasroy.entity.User;
import net.nicholasroy.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nick
 *
 */
@Controller
@PropertySources(value = {@PropertySource("classpath:/controller.properties")})
public class PersistenceController {
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	EntityManager entityManager;
	
	@RequestMapping(method=RequestMethod.GET, value="/persistence")
	public @ResponseBody Iterable<User> persistence(@RequestParam(value="lname", required=false, defaultValue="Test") String lname) {
		Iterable<User> users = repository.findAll();
		return users;
	}
}