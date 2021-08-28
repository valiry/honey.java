package net.valiry.honey.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import net.valiry.honey.HoneyWorld;

public class HoneyReaders {

    private static final short HONEY_MAGIC = (short) 0xAFFE;
    private static final Map<Short, HoneyReader> READER_MAP = new HashMap<>() {
        {
            this.put((short) 0x0000, new HoneyReaderV0());
            this.put((short) 0x0001, new HoneyReaderV1());
        }
    };

    public static void registerReader(final short ver, final HoneyReader reader) {
        READER_MAP.put(ver, reader);
    }

    /**
     * Attempts to read a HoneyWorld from an input stream
     *
     * @param inputStream The input stream
     *
     * @return A HoneyWorld
     *
     * @throws IOException See {@link InputStream#read()}
     */
    public static HoneyWorld read(final InputStream inputStream) throws IOException {
        return read(inputStream.readAllBytes());
    }

    /**
     * Attempts to read a HoneyWorld from a byte array
     *
     * @param bytes The byte array
     *
     * @return A HoneyWorld
     */
    public static HoneyWorld read(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        checkMagic(byteBuffer);

        final short ver = byteBuffer.getShort();
        final HoneyReader reader = READER_MAP.get(ver);
        if (reader == null) {
            throw new IllegalStateException("Could not find implementation for version " + ver);
        }

        return reader.read(byteBuffer);
    }

    private static void checkMagic(final ByteBuffer byteBuffer) {
        if (byteBuffer.getShort() != HONEY_MAGIC) {
            throw new IllegalStateException("Data is not in honey format");
        }
    }

}
