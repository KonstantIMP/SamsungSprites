package kimp.ksprites.org;

/**
 * Supported sprite's move directions
 */
public enum Direction {
    DIRECTION_UP   (0),
    DIRECTION_DOWN (2),
    DIRECTION_LEFT (3),
    DIRECTION_RIGHT(1),
    DIRECTION_STOP (4);

    public final int rowNumber;

    private Direction(int row) {
        rowNumber = row;
    }
}
