package net.valiry.honey.writer;

import com.github.luben.zstd.Zstd;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import net.valiry.honey.HoneyWorld;

public abstract class HoneyWriter {

    public abstract byte[] write(HoneyWorld world) throws IOException;

    protected void writeCompressed(final OutputStream outputStream, final byte[] data) throws IOException {
        final byte[] compressed = Zstd.compress(data);
        outputStream.write(ByteBuffer.allocate(4).putInt(compressed.length).array());
        outputStream.write(ByteBuffer.allocate(4).putInt(data.length).array());
        outputStream.write(compressed);
    }

}
