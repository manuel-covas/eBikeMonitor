package pt.manuelcovas.ebikemonitor.datatypes.bq76930;

public class SysStat {

    public static final int LENGTH = 1;
    public boolean overcurrentDischarge,
                   shortcircuitDischarge,
                   overvoltage,
                   undervoltage,
                   overrideAlert,
                   deviceXReady,
                   coulombCounterReady;

    public SysStat(byte[] payload) {
        overcurrentDischarge = (payload[0] & 0x01) != 0;
        shortcircuitDischarge = (payload[0] & 0x02) != 0;
        overvoltage            = (payload[0] & 0x04) != 0;
        undervoltage           = (payload[0] & 0x08) != 0;
        overrideAlert = (payload[0] & 0x10) != 0;
        deviceXReady = (payload[0] & 0x20) != 0;
        coulombCounterReady = (payload[0] & 0x80) != 0;
    }

    public byte[] serialize() {

        byte result = 0x00;

        result |= (overcurrentDischarge ? 0x01 : 0x00);
        result |= (shortcircuitDischarge ? 0x02 : 0x00);
        result |= (overvoltage            ? 0x04 : 0x00);
        result |= (undervoltage           ? 0x08 : 0x00);
        result |= (overrideAlert ? 0x10 : 0x00);
        result |= (deviceXReady ? 0x20 : 0x00);
        result |= (coulombCounterReady ? 0x80 : 0x00);

        return new byte[] {result};
    }
}
