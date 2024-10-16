package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}
	
	public void saveUser(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		
		if(isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {				
				encodePassword(user);			
			}
			
		} else {
			encodePassword(user);			
		}
			
		userRepository.save(user);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);
		
		if(userByEmail == null)
			return true;
		
		boolean isCreatingNew = (id == null);
		
		// We verify for NOT Unique - we return false:
			// Create New user mode:
		if(isCreatingNew) {
			if(userByEmail != null) 
				return false;
		}
			// Edit user mode:
		else {
			if(userByEmail.getId() != id)
				return false;
		}
		
		
		return true;
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();		
		} catch(NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}
	
	public void deleteUser(Integer id) throws UserNotFoundException {
		Long count = userRepository.countById(id);
		if(count == null || count == 0)
			throw new UserNotFoundException("Could not find any user with ID " + id);
		
		userRepository.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepository.updateEnabledStatus(id, enabled);
	}
	
}
