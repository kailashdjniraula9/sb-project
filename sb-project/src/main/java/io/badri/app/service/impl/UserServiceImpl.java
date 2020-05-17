package io.badri.app.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.badri.app.entity.Mail;
import io.badri.app.entity.User;
import io.badri.app.entity.VerificationToken;
import io.badri.app.repository.UserRepository;
import io.badri.app.repository.VerificationTokenRepository;
import io.badri.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private MailServiceImpl mailService;

	private UserRepository userRepo;

	@Autowired
	public UserServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public void saveUser(User user) {

		User usrWithEmail = userRepo.findByEmail(user.getEmail());
		User usrWithUsername = userRepo.findByUsername(user.getUsername());

		if (usrWithEmail == null && usrWithUsername == null) {

			userRepo.save(user);

			String token = generateVerficationToken(user);

			Mail mail = new Mail();
			mail.setMailFrom("kailasdjtest@gmail.com");
			mail.setMailTo(user.getEmail());
			mail.setMailSubject("Spring Boot - Email Service");
			mail.setMailContent("Learn how to send email using Spring Boot!");
			try {
				mailService.sendMail(mail, token);
			} catch (MessagingException e) {

				e.printStackTrace();
			}

		} else
			throw new RuntimeException("User with given email or username already exists.");
	}

	private String generateVerficationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(Instant.now());

		verificationTokenRepository.save(verificationToken);
		return token;

	}

	@Override
	@Transactional
	public boolean updateUser(User user, int id) {

		if (user != null) {
			user.setId(id);
			userRepo.save(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUser(int id) {

		if (id != 0) {
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

		if (userRepo.findByUsername(uname).isEnabled()) {

			if ((userRepo.findByUsername(uname) != null && userRepo.findByPassword(password) != null)
					|| (userRepo.findByPassword(password) != null && userRepo.findByEmail(email) != null)) {

				return true;
			}

		}

		return false;

	}

	@Override
	@Transactional
	public void verifyAccount(String token) {

		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);

		verificationToken.orElseThrow();

		fetchUserandEnable(verificationToken.get());

	}

	private void fetchUserandEnable(VerificationToken verificationToken) {

		String username = verificationToken.getUser().getUsername();

		User user = userRepo.findByUsername(username);

		user.setEnabled(true);

		userRepo.save(user);

	}

}
