package io.badri.app.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.badri.app.entity.Mail;
import io.badri.app.entity.User;
import io.badri.app.entity.VerificationToken;
import io.badri.app.repository.UserRepository;
import io.badri.app.repository.VerificationTokenRepository;
import io.badri.app.service.MailService;
import io.badri.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;

	@Autowired
	public UserServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Autowired
	private MailService mailService;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public void saveUser(User user) {

		User usrWithEmail = userRepo.findByEmail(user.getEmail());
		User usrWithUsername = userRepo.findByUsername(user.getUsername());

		// if returned null there is no user exists already
		if (usrWithEmail == null && usrWithUsername == null) {
			userRepo.save(user);

			// send email with token
			String token = generateVerficationToken(user);

			Mail mail = new Mail();

			mail.setMailFrom("spring.navigate@gmail.com");
			mail.setMailTo(user.getEmail());
			mail.setMailSubject("Verify email");
			mail.setMailContent("Activate the account by clicking this link below.");

			try {
				mailService.sendMail(mail, token);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else
			throw new RuntimeException("User with given email or username already exists.");
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

		if ((userRepo.findByUsername(uname) != null && userRepo.findByPassword(password) != null)
				|| (userRepo.findByPassword(password) != null && userRepo.findByEmail(email) != null)) {

			return true;
		}

		return false;

	}

	private String generateVerficationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);

		Calendar currentDate = Calendar.getInstance();

		currentDate.add(Calendar.DATE, 1);

		verificationToken.setExpiryDate(currentDate.getTime());

		verificationTokenRepository.save(verificationToken);
		return token;

	}

	@Override
	@Transactional
	public void verifyAccount(String token) {

		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		VerificationToken vToken = getVerificationToken(token);

		Calendar currentDate = Calendar.getInstance();

		if ((vToken.getExpiryDate().getTime() - currentDate.getTime().getTime()) <= 0) {

			deleteExpiredTokenDetails(verificationToken);

			throw new RuntimeException("Token has expired");

		}

		else
			fetchUserandEnable(verificationToken);

	}

	private void fetchUserandEnable(VerificationToken verificationToken) {

		String username = verificationToken.getUser().getUsername();

		User user = userRepo.findByUsername(username);

		user.setEnabled(true);

		userRepo.save(user);

		verificationTokenRepository.deleteByUser(user);

	}

	private void deleteExpiredTokenDetails(VerificationToken verificationToken) {

		int id = verificationToken.getUser().getId();

		userRepo.deleteById(id);

	}

	@Override
	public VerificationToken getVerificationToken(String VerificationToken) {
		return verificationTokenRepository.findByToken(VerificationToken);
	}
}
