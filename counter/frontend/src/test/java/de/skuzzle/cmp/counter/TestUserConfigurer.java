package de.skuzzle.cmp.counter;

import static org.mockito.Mockito.when;

import de.skuzzle.cmp.auth.TallyUser;

public class TestUserConfigurer {

    private final TallyUser currentUserMock;

    public TestUserConfigurer(TallyUser currentUserMock) {
        this.currentUserMock = currentUserMock;
    }

    public void anonymous() {
        when(currentUserMock.isLoggedIn()).thenReturn(false);
        when(currentUserMock.getName()).thenReturn("unknown");
    }

    public void authenticatedWithName(String name) {
        when(currentUserMock.isLoggedIn()).thenReturn(true);
        when(currentUserMock.getName()).thenReturn(name);
    }
}
