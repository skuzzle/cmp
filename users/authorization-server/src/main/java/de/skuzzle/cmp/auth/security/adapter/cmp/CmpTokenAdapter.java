package de.skuzzle.cmp.auth.security.adapter.cmp;

import java.util.Map;

import de.skuzzle.cmp.auth.security.adapter.TokenAdapter;

public class CmpTokenAdapter implements TokenAdapter {

    @Override
    public Class<CmpUserDetails> getPrincipalType() {
        return CmpUserDetails.class;
    }

    @Override
    public Map<String, Object> getAdditionalClaims(Object principal) {
        final CmpUserDetails cmpUserDetails = (CmpUserDetails) principal;
        return Map.of(
                "sub", cmpUserDetails.getUsername(),
                "full_name", cmpUserDetails.getFullName(),
                "account_type", "google");
    }

}
