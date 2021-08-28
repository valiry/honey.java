package net.valiry.honey.reader;

import com.github.luben.zstd.Zstd;
import java.nio.ByteBuffer;
import net.valiry.honey.HoneyWorld;

public abstract class HoneyReader {

    protected abstract HoneyWorld read(ByteBuffer byteBuffer);

    protected byte[] readCompressed(final ByteBuffer byteBuffer) {
        final int len = byteBuffer.getInt();
        final int ogLen = byteBuffer.getInt();

        final byte[] bytes = new byte[len];
        byteBuffer.get(bytes);

        return Zstd.decompress(bytes, ogLen);
    }

}
