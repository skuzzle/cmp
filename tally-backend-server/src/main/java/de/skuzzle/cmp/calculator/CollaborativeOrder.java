package de.skuzzle.cmp.calculator;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

public class CollaborativeOrder {

    @Id
    private String id;
    @Version
    private int version;

    private Cart cart;
}
