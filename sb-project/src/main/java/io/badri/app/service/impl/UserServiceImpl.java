package io.badri.app.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.badri.app.entity.User;
import io.badri.app.repository.UserRepository;
import io.badri.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;

	@Autowired
	public UserServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public void saveUser(User user) {

		User usrWithEmail = userRepo.findByEmail(user.getEmail());
		User usrWithUsername = userRepo.findByUsername(user.getUsername());

		// if returned null there is no user exists already
		if (usrWithEmail == null && usrWithUsername == null) {
			userRepo.save(user);
			
		} else
			throw new RuntimeException("User with given email or username already exists.");
	}

	@Override
	@Transactional
	public boolean updateUser(User user,int id) {
		//User u = userRepo.findById(id);
		
		/*
		User u = userRepo.findById(id);
		 if(u != null) {
			 u.setFirstName(user.getFirstName());
			 u.setLastName(user.getLastName());
			 u.setDob(user.getDob());
			 u.setEmail(user.getEmail());
			 u.setUsername(user.getUsername());
			// u.setId(id);
			 System.out.println("updated id " + user.getId());
			 return true;
		 }
		*/
		if(user != null) {
			user.setId(id);
			userRepo.save(user);
			return true;
		}
			return false;
 	}

	@Override
	public boolean deleteUser(int id) {
		
		if(id!=0) {
			userRepo.deleteById(id);
            return true;	
		}
		
		return false;
	}

	@Override
	public List<User> listUser() {
		return userRepo.findAll();
	}

	@Override
	public boolean getLoggedinUser(User user) {

		String uname = user.getUsername();
		String email = user.getEmail();
		String password = user.getPassword();
		
	    if((userRepo.findByUsername(uname)!=null && userRepo.findByPassword(password)!=null)
	    		|| (userRepo.findByPassword(password)!=null && userRepo.findByEmail(email)!=null))	{
	    	
	    	return true;
	    }
			
	    return false;
		
	}

}
