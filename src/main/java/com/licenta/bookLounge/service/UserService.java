package com.licenta.bookLounge.service;

import com.licenta.bookLounge.model.User;
import com.licenta.bookLounge.repository.UserRepository;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

        private final RepositoryService repositoryService;

	public User getUserByEmail(String email) {
           return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public void saveUser(User user) {
           MongoDatabase mongoDatabase = repositoryService.getDatabase();
           userRepository.save(user);
	}

}
