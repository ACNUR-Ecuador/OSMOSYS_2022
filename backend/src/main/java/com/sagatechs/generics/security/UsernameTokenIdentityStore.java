package com.sagatechs.generics.security;

import com.sagatechs.generics.security.credentials.UsernameJwtCredential;
import com.sagatechs.generics.security.servicio.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.EnumSet;
import java.util.Set;

@SuppressWarnings("unused")
@ApplicationScoped
public class UsernameTokenIdentityStore implements IdentityStore {

	@Inject
	UserService userService;

	@Override
	public CredentialValidationResult validate(Credential credential) {
		CredentialValidationResult credentialValidationResult;
		if (credential instanceof UsernameJwtCredential) {
			UsernameJwtCredential usernamePasswordCredential = (UsernameJwtCredential) credential;
			credentialValidationResult = new CredentialValidationResult(usernamePasswordCredential.getUsername());
		}else {
			credentialValidationResult = CredentialValidationResult.NOT_VALIDATED_RESULT;
		}
		return credentialValidationResult;
	}

	@Override
	public Set<ValidationType> validationTypes() {
		return EnumSet.of(ValidationType.VALIDATE);
	}

	@Override
	public int priority() {
		return 100;
	}

}
