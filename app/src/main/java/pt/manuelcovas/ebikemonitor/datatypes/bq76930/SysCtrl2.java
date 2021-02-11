package pt.manuelcovas.ebikemonitor.datatypes.bq76930;

public class SysCtrl2 {

    public static int LENGTH = 1;
    public boolean chgOn,
                   dsgOn,
                   ccOneshot,
                   ccEn,
                   delayDis;

    public SysCtrl2(byte[] payload) {
        chgOn = (payload[0] & 0x01) != 0;
        dsgOn = (payload[0] & 0x02) != 0;
        ccOneshot = (payload[0] & 0x20) != 0;
        ccEn = (payload[0] & 0x40) != 0;
        delayDis = (payload[0] & 0x80) != 0;
    }

    public byte[] serialize() {

        byte result = 0x00;

        result |= (chgOn ? 0x01 : 0x00);
        result |= (dsgOn ? 0x02 : 0x00);
        result |= (ccOneshot ? 0x20 : 0x00);
        result |= (ccEn ? 0x40 : 0x00);
        result |= (delayDis ? 0x80 : 0x00);

        return new byte[] {result};
    }
}
