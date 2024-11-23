package antonchuvashov.model;

public class Entry {
    private int entryId;
    private String name;

    public Entry(int entryId, String name) {
        this.entryId = entryId;
        this.name = name;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
