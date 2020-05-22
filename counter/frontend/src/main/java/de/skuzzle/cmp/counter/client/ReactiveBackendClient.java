package de.skuzzle.cmp.counter.client;

import org.springframework.web.reactive.function.client.WebClient;

public class ReactiveBackendClient implements BackendClient {

    private final WebClient client;

    public ReactiveBackendClient(WebClient client) {
        this.client = client;
    }

    @Override
    public boolean isHealthy() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public RestTallyMetaInfoResponse getMetaInfo() {
        return client.get()
                .uri("_meta")
                .retrieve()
                .bodyToMono(RestTallyMetaInfoResponse.class)
                .block();
    }

    @Override
    public RestTallySheetsReponse listTallySheets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RestTallyResponse createNewTallySheet(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RestTallyResponse getTallySheet(String publicKey, Filter filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void increment(String adminKey, RestTallyIncrement increment) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteTallySheet(String adminKey) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteIncrement(String adminKey, String incrementId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateIncrement(String adminKey, RestTallyIncrement increment) {
        // TODO Auto-generated method stub

    }

    @Override
    public void assignToCurrentUser(String adminKey) {
        // TODO Auto-generated method stub

    }

    @Override
    public void changeName(String adminKey, String newTitle) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addShare(String adminKey, RestShareInformation shareInformation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteShare(String adminKey, String shareId) {
        // TODO Auto-generated method stub

    }

}
