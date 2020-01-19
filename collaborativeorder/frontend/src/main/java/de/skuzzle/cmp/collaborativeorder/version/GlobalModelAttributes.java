package de.skuzzle.cmp.collaborativeorder.version;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
class GlobalModelAttributes {

    private final Version version;

    public GlobalModelAttributes(Version version) {
        this.version = version;
    }

    @ModelAttribute("version")
    public Version getVersion() {
        return version;
    }
}
