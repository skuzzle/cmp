package de.skuzzle.cmp.collaborativeorder.domain;

public class UnknownParticipantException extends RuntimeException {

    private static final long serialVersionUID = -4069756205627261865L;

    public UnknownParticipantException(String message) {
        super(message);
    }

}
