package net.valiry.honey.reader;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import net.valiry.honey.HoneyChunk;
import net.valiry.honey.HoneyWorld;

public class HoneyReaderV1 extends HoneyReader {

    @Override
    protected HoneyWorld readData(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        if (byteBuffer.getShort() != (short) 0xAFFE) {
            throw new IllegalStateException("Invalid data");
        }
        if (byteBuffer.getShort() != 1) {
            throw new IllegalStateException("Invalid version");
        }
        return this.read(byteBuffer);
    }

    @Override
    protected HoneyWorld read(final ByteBuffer byteBuffer) {
        final HoneyWorld honeyWorld = new HoneyWorld();

        // Chunks
        final int storedChunks = byteBuffer.getInt();
        for (int i = 0; i < storedChunks; i++) {
            honeyWorld.put(this.readChunk(byteBuffer));
        }

        // Meta
        final byte[] meta = byteBuffer.get() > 0 ? this.readCompressed(byteBuffer) : null;
        honeyWorld.setMetadataNbt(meta);

        return honeyWorld;
    }

    private HoneyChunk readChunk(final ByteBuffer byteBuffer) {
        // Chunk header
        final int x = byteBuffer.getInt();
        final int z = byteBuffer.getInt();
        final BitSet populatedSectionsSet = BitSet.valueOf(new byte[] {byteBuffer.get(), byteBuffer.get()});

        // Sections
        final Map<Integer, HoneyChunk.HoneyChunkSection> sectionMap = new HashMap<>();
        for (int y = 0; y < populatedSectionsSet.length(); y++) {
            // Skip if not populated
            if (!populatedSectionsSet.get(y)) {
                continue;
            }

            // Read states
            final short[] states = new short[4096];
            for (int idx = 0; idx < states.length; idx++) {
                states[idx] = byteBuffer.getShort();
            }

            sectionMap.put(y, new HoneyChunk.HoneyChunkSection(states));
        }

        // Entities and tile entities
        final byte[] entityNbt = byteBuffer.get() > 0 ? this.readCompressed(byteBuffer) : null;
        final byte[] tileEntityNbt = byteBuffer.get() > 0 ? this.readCompressed(byteBuffer) : null;

        return new HoneyChunk(HoneyChunk.ChunkId.of(x, z), sectionMap, entityNbt, tileEntityNbt);
    }

}
