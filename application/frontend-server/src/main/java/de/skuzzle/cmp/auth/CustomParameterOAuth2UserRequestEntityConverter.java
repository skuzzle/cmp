package de.skuzzle.cmp.auth;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import java.net.URI;
import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Preconditions;

/**
 * Copy if spring's default implementation which allows to customize the form's parameter
 * name.
 *
 * @author Simon Taddiken
 */
public class CustomParameterOAuth2UserRequestEntityConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {

    private static final MediaType DEFAULT_CONTENT_TYPE = MediaType
            .valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

    // This is the default which is also used by by spring's
    // OAuth2UserRequestEntityConverter. Unfortunately that value doesn't play well with
    // spring's own authorization server which expects the value to be 'token'
    private String tokenParameterName;

    public CustomParameterOAuth2UserRequestEntityConverter() {
        this(OAuth2ParameterNames.ACCESS_TOKEN);
    }

    public CustomParameterOAuth2UserRequestEntityConverter(String tokenParameterName) {
        setTokenParameterName(tokenParameterName);
    }

    public void setTokenParameterName(String tokenParameterName) {
        Preconditions.checkArgument(tokenParameterName != null, "tokenParameterName must not be null");
        this.tokenParameterName = tokenParameterName;
    }

    /**
     * Returns the {@link RequestEntity} used for the UserInfo Request.
     *
     * @param userRequest the user request
     * @return the {@link RequestEntity} used for the UserInfo Request
     */
    @Override
    public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
        final ClientRegistration clientRegistration = userRequest.getClientRegistration();

        final HttpMethod httpMethod = HttpMethod.POST;
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        final URI uri = UriComponentsBuilder
                .fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .build()
                .toUri();

        headers.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
        headers.setContentType(DEFAULT_CONTENT_TYPE);
        final MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(tokenParameterName, userRequest.getAccessToken().getTokenValue());
        return new RequestEntity<>(formParameters, headers, httpMethod, uri);
    }
}
