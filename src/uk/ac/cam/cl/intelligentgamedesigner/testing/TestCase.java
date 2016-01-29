package uk.ac.cam.cl.intelligentgamedesigner.testing;

public abstract class TestCase {

    protected String description;

    public TestCase (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void run ();
}
