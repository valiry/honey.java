package net.valiry.honey.writer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.valiry.honey.HoneyWorld;

public class HoneyWriters {

    private static final Map<Short, HoneyWriter> WRITER_MAP = new HashMap<>() {
        {
            this.put((short) 0x0001, new HoneyWriterV1());
        }
    };
    private static final short LATEST_VER = 0x0001;

    public static byte[] writeWithLatest(final HoneyWorld world) throws IOException {
        return WRITER_MAP.get(LATEST_VER).write(world);
    }

    public static HoneyWriter get(final short ver) {
        return WRITER_MAP.get(ver);
    }

}
