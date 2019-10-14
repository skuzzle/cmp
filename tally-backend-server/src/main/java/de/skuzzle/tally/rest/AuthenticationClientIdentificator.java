package de.skuzzle.tally.rest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import de.skuzzle.tally.rest.auth.TallyUser;
import de.skuzzle.tally.rest.ratelimit.ApiClient;
import de.skuzzle.tally.rest.ratelimit.ClientIdentificator;
import de.skuzzle.tally.rest.ratelimit.RemoteIpClientIdentificator;

class AuthenticationClientIdentificator implements ClientIdentificator<HttpServletRequest> {
    
    private final ClientIdentificator<HttpServletRequest> identifyByIp = new RemoteIpClientIdentificator();

    @Override
    public Optional<ApiClient> tryIdentifyClientFrom(HttpServletRequest hint) {
        final TallyUser user = TallyUser.fromCurrentAuthentication();
        if (user.isAnonymous()) {
            return identifyByIp.tryIdentifyClientFrom(hint);
        }
        return Optional.of(ApiClient.identifiedBy(user));
    }

}
