package io.badri.app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.badri.app.entity.User;
import io.badri.app.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PutMapping("/{userId}")   
	public void updateUser(@PathVariable(value = "userId") Integer userId, @RequestBody User user) {
		//update 
		boolean b = userService.updateUser(user,userId);
		if(b) System.out.println("updated");
		else System.out.println("failed");
		//users/1
	}
	
	@GetMapping
	public List<User> getAllUsers() {
		
		return userService.listUser();
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable(value = "userId") int userId) {
		
		userService.deleteUser(userId);
	}
	
}
