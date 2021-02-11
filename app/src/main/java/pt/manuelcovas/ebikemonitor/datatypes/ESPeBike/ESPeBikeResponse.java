package pt.manuelcovas.ebikemonitor.datatypes.ESPeBike;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ESPeBikeResponse {

    public static int LENGTH = 1 + 2 * 4;

    public ESPeBikeResponseType eBikeResponseType;
    public ESPeBikeErrorType eBikeErrorType;
    public int espError;
    public boolean hasError;

    public byte[] extraData = {};

    public ESPeBikeResponse(byte[] payload) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN);
        eBikeResponseType = ESPeBikeResponseType.values()[byteBuffer.get()];
        espError = byteBuffer.getInt();
        eBikeErrorType = ESPeBikeErrorType.values()[byteBuffer.getInt()];
        hasError = (espError != 0) || (eBikeErrorType != ESPeBikeErrorType.EBIKE_OK);

        if (payload.length > 9)
            extraData = Arrays.copyOfRange(payload, 9, payload.length);
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(espError);
        byteBuffer.putInt(eBikeErrorType.ordinal());
        return byteBuffer.array();
    }
}
