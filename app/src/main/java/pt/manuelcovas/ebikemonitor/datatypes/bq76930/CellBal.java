package pt.manuelcovas.ebikemonitor.datatypes.bq76930;

public class CellBal {

    public static int LENGTH = 2;  // BQ76920: 1, BQ76930: 2, BQ76940: 3
    public boolean[] cellBalancingStatuses = new boolean[LENGTH *5];

    public CellBal(byte[] payload) {

        for (int i1 = 0; i1 < LENGTH; i1++)
            for (int i2 = 0; i2 < 5; i2++)
                cellBalancingStatuses[i1 * 5 + i2] = (payload[i1] & (0x01 << i2)) != 0;
    }

    public byte[] serialize() {

        byte[] result = new byte[LENGTH];

        for (int i1 = 0; i1 < LENGTH; i1++)
            for (int i2 = 0; i2 < 5; i2++)
                result[i1] |= (cellBalancingStatuses[i1 * 5 + i2] ? (0x01 << i2) : 0x00);

        return result;
    }
}
