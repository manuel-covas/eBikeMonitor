package pt.manuelcovas.ebikemonitor.datatypes.ESPeBike;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ESPeBikeError {

    public static int LENGTH = 2 * 4;
    public ESPeBikeErrorType eBikeErrorType;
    public int espError;

    public ESPeBikeError(byte[] payload) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN);
        espError = byteBuffer.getInt();
        eBikeErrorType = ESPeBikeErrorType.values()[byteBuffer.getInt()];
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(espError);
        byteBuffer.putInt(eBikeErrorType.ordinal());
        return byteBuffer.array();
    }
}
