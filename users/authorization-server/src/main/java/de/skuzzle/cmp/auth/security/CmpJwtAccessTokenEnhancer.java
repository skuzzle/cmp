package de.skuzzle.cmp.auth.security;

import java.util.Map;

class CmpJwtAccessTokenEnhancer extends AdditionalClaimsJwtAccessTokenConverter<CmpUserDetails> {

    @Override
    protected Class<CmpUserDetails> getUserDetailsType() {
        return CmpUserDetails.class;
    }

    @Override
    protected Map<String, Object> getAdditionalClaims(CmpUserDetails userDetails) {
        return Map.of("sub", userDetails.getFullName());
    }

}
