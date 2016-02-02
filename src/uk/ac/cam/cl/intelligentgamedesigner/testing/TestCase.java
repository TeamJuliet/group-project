package uk.ac.cam.cl.intelligentgamedesigner.testing;

import java.io.Serializable;

public abstract class TestCase implements Serializable {

    protected String description;
    protected String fileName;

    public TestCase (String description, String fileName) {
        this.description = description;
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }
    public String getFileName() { return fileName; }

    public abstract boolean run ();
}
