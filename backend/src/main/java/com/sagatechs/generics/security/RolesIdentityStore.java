package com.sagatechs.generics.security;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class RolesIdentityStore implements IdentityStore {

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {

        return new HashSet<>();// roles =this.userService.getRolesNamesByUsername(validationResult.getCallerPrincipal().getName(), State.ACTIVE);
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.PROVIDE_GROUPS);
    }

}
