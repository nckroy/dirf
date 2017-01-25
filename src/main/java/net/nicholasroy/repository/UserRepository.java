package net.nicholasroy.repository;

import java.util.List;

import net.nicholasroy.entity.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findByLname(String lname);
	
}