package pt.manuelcovas.ebikemonitor.datatypes.bq76930;

public class SysCtrl1 {

    public static int LENGTH = 1;
    public boolean shutB,
                   shutA,
                   tempSel,
                   adcEn,
                   loadPresent;

    public SysCtrl1(byte[] payload) {
        shutB = (payload[0] & 0x01) != 0;
        shutA = (payload[0] & 0x02) != 0;
        tempSel = (payload[0] & 0x08) != 0;
        adcEn = (payload[0] & 0x10) != 0;
        loadPresent = (payload[0] & 0x80) != 0;
    }

    public byte[] serialize() {

        byte result = 0x00;

        result |= (shutB ? 0x01 : 0x00);
        result |= (shutA ? 0x02 : 0x00);
        result |= (tempSel ? 0x08 : 0x00);
        result |= (adcEn ? 0x10 : 0x00);
        result |= (loadPresent ? 0x80 : 0x00);

        return new byte[] {result};
    }
}
