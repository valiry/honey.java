package net.valiry.honey.reader;

import com.github.luben.zstd.Zstd;
import java.nio.ByteBuffer;
import net.valiry.honey.HoneyWorld;

public abstract class HoneyReader {

    /**
     * Attempt to read honey data (including magic and version)
     *
     * @param bytes Byte array to read from
     *
     * @return Honey data
     */
    protected abstract HoneyWorld readData(byte[] bytes);

    /**
     * Attempt to read honey data (excluding magic and version)
     *
     * @param byteBuffer Byte buffer to read from
     *
     * @return Honey data
     */
    protected abstract HoneyWorld read(ByteBuffer byteBuffer);

    protected byte[] readCompressed(final ByteBuffer byteBuffer) {
        final int len = byteBuffer.getInt();
        final int ogLen = byteBuffer.getInt();

        final byte[] bytes = new byte[len];
        byteBuffer.get(bytes);

        return Zstd.decompress(bytes, ogLen);
    }

}
