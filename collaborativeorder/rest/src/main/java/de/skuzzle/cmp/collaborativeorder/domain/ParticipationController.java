package de.skuzzle.cmp.collaborativeorder.domain;

import de.skuzzle.cmp.rest.auth.TallyUser;

public class ParticipationController {

    private final TallyUser tallyUser;
    private final ParticipationService participationService;

    public ParticipationController(TallyUser tallyUser, ParticipationService participationService) {
        this.tallyUser = tallyUser;
        this.participationService = participationService;
    }

    private UserId currentUser() {
        return UserId.of(tallyUser.getSource(), tallyUser.getId(), tallyUser.isAnonymous());
    }

    public RestParticipant getParticipation(String collaborativeOrderId) {
        final UserId currentUser = currentUser();
        final Participant participation = participationService.getParticipation(collaborativeOrderId, currentUser);
        return RestParticipant.fromDomain(participation);
    }

    public void join(String collaborativeOrderId) {
        final UserId currentUser = currentUser();
        participationService.join(collaborativeOrderId, currentUser);
    }

    public void leave(String collaborativeOrderId) {
        final UserId currentUser = currentUser();
        participationService.leave(collaborativeOrderId, currentUser);
    }

}
