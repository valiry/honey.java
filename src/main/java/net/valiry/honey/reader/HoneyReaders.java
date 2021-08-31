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
            this.put((short) 0x0002, new HoneyReaderV2());
        }
    };

    public static void registerReader(final short ver, final HoneyReader reader) {
        READER_MAP.put(ver, reader);
    }

    /**
     * Attempts to read a HoneyWorld from an input stream
     * Tries to choose the correct reader by detecting the version
     *
     * @param inputStream The input stream
     *
     * @return A HoneyWorld
     *
     * @throws IOException See {@link InputStream#read()}
     */
    public static HoneyWorld detectAndRead(final InputStream inputStream) throws IOException {
        return detectAndRead(inputStream.readAllBytes());
    }

    /**
     * Attempts to read a HoneyWorld from a byte array
     * Tries to choose the correct reader by detecting the version
     *
     * @param bytes The byte array
     *
     * @return A HoneyWorld
     */
    public static HoneyWorld detectAndRead(final byte[] bytes) {
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

    public static HoneyReader get(final short ver) {
        return READER_MAP.get(ver);
    }

}
