public class Tile {
    private int value;

    public Tile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public boolean equals(Tile other) {
        return this.value == other.value;
    }

    public void merge(Tile other) {
        this.value *= 2;
    }
}
