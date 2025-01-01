package com.koyta.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.koyta.entity.User;
import com.koyta.util.CommonUtil;

public class AuditAwareConfig implements AuditorAware<Integer>{

	@Override
	public Optional<Integer> getCurrentAuditor() {
		
		User loggedInUser = CommonUtil.getLoggedInUser();
		
		return Optional.of(loggedInUser.getId());
	}

}
