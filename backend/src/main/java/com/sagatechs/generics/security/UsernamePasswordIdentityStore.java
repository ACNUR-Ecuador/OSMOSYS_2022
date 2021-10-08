package com.sagatechs.generics.security;

import com.sagatechs.generics.security.credentials.UsernamePasswordRolesCredential;
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
public class UsernamePasswordIdentityStore implements IdentityStore {

    @Inject
    UserService userService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        CredentialValidationResult credentialValidationResult;
        if (credential instanceof UsernamePasswordRolesCredential) {
            UsernamePasswordRolesCredential credentialPass = (UsernamePasswordRolesCredential) credential;

            if(this.userService.verifyUsernamePassword(credentialPass.getCaller(), credentialPass.getPasswordAsString())!=null){
                credentialValidationResult= new CredentialValidationResult(credentialPass.getCaller());
            }else{
                credentialValidationResult = CredentialValidationResult.INVALID_RESULT;
            }
        } else {
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
        return 200;
    }

}