package Districts;

public enum DistrictsColor {
    RED("red"),
    YELLOW("yellow"),
    GREEN("green"),
    BLUE("blue"),
    PURPLE("purple");

    private final String displayName;

    DistrictsColor(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
