package com.assignment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.entity.LogoutToken;
import com.assignment.repository.LogoutTokenRepository;

@Service
public class LogoutTokenService {
	
	@Autowired
	LogoutTokenRepository logoutTokenRepository;
	
	public boolean checkIfTokenExists(String token) {
		return logoutTokenRepository.existsByToken(token);
	}
	
	public LogoutToken createToken(LogoutToken logoutToken) {
		return logoutTokenRepository.save(logoutToken);
	}
	
	public List<LogoutToken> createTokens(List<LogoutToken> logoutTokenList) {
		return logoutTokenRepository.saveAllAndFlush(logoutTokenList);
	}
	
	public List<LogoutToken> getAllTokens() {
		return logoutTokenRepository.findAll();
	}
	
	public void deleteAllTokens() {
		logoutTokenRepository.deleteAllInBatch();
	}

}
