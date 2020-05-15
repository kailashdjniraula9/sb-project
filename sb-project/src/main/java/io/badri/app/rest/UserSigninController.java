package io.badri.app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.badri.app.entity.User;
import io.badri.app.service.UserService;

@RestController
@RequestMapping("/users")
public class UserSigninController {

	private UserService userService;

	@Autowired
	public UserSigninController(UserService userService) {
	
		this.userService = userService;
	}
	
	@PostMapping("/signup")  // /users/signup
	public void userSignin(@RequestBody User user) {
		userService.saveUser(user);
	}

}
