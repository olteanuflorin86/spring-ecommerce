package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat; 
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Disabled
	@Test
	void testCreateNewUserWithOneRole() {
		Role roleAdmin = testEntityManager.find(Role.class, 1);
		User testUser = new User("johndoe@gmail.com", "pass1234", "John", "Doe");
		testUser.addRole(roleAdmin);
		User savedTestUser = userRepository.save(testUser);
		assertThat(savedTestUser.getId()).isGreaterThan(0);
		assertNotNull(savedTestUser);
	}
	
	@Disabled
	@Test
	void testCreateNewUserWithTwoRoles() {
		User testUser = new User("janedoe@gmail.com", "pass1234", "Jane", "Doe");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		testUser.addRole(roleAssistant);
		testUser.addRole(roleEditor);
		User savedTestUser = userRepository.save(testUser);
		assertThat(savedTestUser.getId()).isGreaterThan(0);
		assertNotNull(savedTestUser);
	}
	
	@Disabled
	@Test
	void testListAllUsers() {
		List<User> users = new ArrayList<>();
//		userRepository.findAll().forEach(user -> {
//			users.add(user);
//		});
		userRepository.findAll().forEach(users::add);
		System.out.println(users);
//		assertThat(users.size()).isEqualTo(2);
		assertThat(users.size()).isGreaterThan(0);
	}
	
	@Disabled
	@Test
	void testGetUserById() {
		User user = userRepository.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Disabled
	@Test
	void testUpdateUserDetails() {
		User user = userRepository.findById(1).get();
		user.setPassword("pass12345");
		user.setEnabled(true);
		user = userRepository.save(user);
		System.out.println(user);
		assertThat(user.getPassword()).isEqualTo("pass12345");
		assertThat(user.isEnabled()).isEqualTo(true);
	}

	@Disabled
	@Test
	void testUpdateUserRoles() {
		User user = userRepository.findById(2).get();
		Role roleShipper = new Role(4);
		user.addRole(roleShipper);
		Role roleEditor = new Role(3);
		user.getRoles().remove(roleEditor);
		user = userRepository.save(user);
		System.out.println(user);
		assertThat(user.getRoles().size()).isGreaterThan(1);
	}
	
	@Disabled
	@Test
	void testDeleteUser() {
		userRepository.deleteById(2);
		User deletedUser = userRepository.findById(2).get();
		assertNull(deletedUser);
	}
	
	@Disabled
	@Test
	public void testGetUserByEmail() {
//		String email = "newUser2@gmail.com";
		String email = "newUser@gmail.com";
		User user = userRepository.getUserByEmail(email);
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
//		Integer id = 100;
		Integer id = 1;
		Long count = userRepository.countById(id);
		assertThat(count).isNotNull().isGreaterThan(0);
	}

}
