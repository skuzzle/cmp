package de.skuzzle.cmp.auth.security.adapter;

import java.util.Map;

public interface TokenAdapter {

    Class<?> getPrincipalType();

    Map<String, Object> getAdditionalClaims(Object principal);

}
