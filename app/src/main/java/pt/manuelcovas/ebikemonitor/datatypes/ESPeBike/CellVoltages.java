package pt.manuelcovas.ebikemonitor.datatypes.ESPeBike;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CellVoltages {

    private static int CELL_COUNT = 10;  // BQ76920: 5, BQ76930: 10, BQ76940: 15
    public static int LENGTH = CELL_COUNT * 8;

    public double[] cellVoltages = new double[CELL_COUNT];

    public CellVoltages(byte[] payload) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < CELL_COUNT; i++)
            cellVoltages[i] = byteBuffer.getDouble();
    }

    public byte[] serialize() {

        ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < CELL_COUNT; i++)
            byteBuffer.putDouble(cellVoltages[i]);
        return byteBuffer.array();
    }
}
