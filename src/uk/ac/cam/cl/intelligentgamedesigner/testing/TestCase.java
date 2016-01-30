package uk.ac.cam.cl.intelligentgamedesigner.testing;

import java.io.Serializable;

public abstract class TestCase implements Serializable {

    protected String description;

    public TestCase (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void run ();
}
