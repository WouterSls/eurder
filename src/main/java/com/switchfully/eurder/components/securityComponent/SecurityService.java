package com.switchfully.eurder.components.securityComponent;


import com.switchfully.eurder.exception.*;

import com.switchfully.eurder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
class SecurityService implements ISecurityService {

	private final UserRepository userRepository;
	private final Utils utils;

	@Autowired
	public SecurityService(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.utils = new Utils();
	}


	public UUID getCustomerUUIDFromAuth(String auth){
		UuidPassword uuidPassword;
		if (auth == null){
			throw new MandatoryFieldException("Please provide a customerID");
		}
		try{
			uuidPassword = getUuidPassword(auth);
		}catch (IllegalArgumentException e){
			throw new IllegalIdException("Please provide a valid customerID");
		}

		return uuidPassword.getUuid();
	}


	public void validateAuthorization(String auth, Feature feature){
		if(auth == null){
			throw new UnauthorizedException("No authorization provided.");
		}

		UuidPassword uuidPassword;

		try {
			uuidPassword = getUuidPassword(auth);
		} catch(IllegalArgumentException exception){
			throw new UnauthorizedException("No valid UUID was provided.");
		}

		User user = userRepository.getUserByUuid(uuidPassword.getUuid())
				.orElseThrow(() -> new UserNotFoundException("No user found with UUID: " + uuidPassword.getUuid()));

		if (!user.doesPasswordMatch(uuidPassword.getPassword())) {
			throw new UnauthorizedException("Wrong password for user used.");
		}
		if (!user.hasAccessTo(feature)) {
			throw new UnauthorizedException("User has no access to this feature.");
		}

	}

	private UuidPassword getUuidPassword(String auth) {
		String decodedUsernamePassword = new String(Base64.getDecoder().decode(auth.substring("basic ".length())));
		UUID uuid = UUID.fromString(decodedUsernamePassword.substring(0, decodedUsernamePassword.indexOf(":")));
		String password = decodedUsernamePassword.substring(decodedUsernamePassword.indexOf(":") + 1);
		return new UuidPassword(uuid, password);
	}

	private void verifyUUID(String auth){
		if (auth == null){
			throw new MandatoryFieldException("Please provide a correct Customer UUID");
		}
		if (!utils.isValidUUIDFormat(auth)){
			throw new InvalidIdFormatException("Please provide a valid UUID format");
		}
	}
}
