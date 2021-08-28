package net.valiry.honey.reader;

import java.nio.ByteBuffer;
import net.valiry.honey.HoneyWorld;

/**
 * Dummy implementation that throws an exception
 */
public class HoneyReaderV0 extends HoneyReader {

    @Override
    protected HoneyWorld read(final ByteBuffer byteBuffer) {
        throw new IllegalStateException("Can't read version 0 (reserved for development)");
    }

}
