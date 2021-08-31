package net.valiry.honey.reader;

import com.github.luben.zstd.Zstd;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import net.valiry.honey.HoneyChunk;
import net.valiry.honey.HoneyWorld;

public class HoneyReaderV2 extends HoneyReader {

    @Override
    protected HoneyWorld readData(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        if (byteBuffer.getShort() != (short) 0xAFFE) {
            throw new IllegalStateException("Invalid data");
        }
        if (byteBuffer.getShort() != 2) {
            throw new IllegalStateException("Invalid version");
        }
        return this.read(byteBuffer);
    }

    @Override
    protected HoneyWorld read(ByteBuffer byteBuffer) {
        final HoneyWorld honeyWorld = new HoneyWorld();

        final int compLen;
        final int uncompLen;

        // Header
        final short protocolVer = byteBuffer.getShort();
        honeyWorld.setProtocolVersion(protocolVer);
        final byte compScheme = byteBuffer.get();
        if (compScheme == 1) {
            compLen = byteBuffer.getInt();
            uncompLen = byteBuffer.getInt();
        } else if (compScheme != 0) {
            throw new IllegalStateException("Unknown compression scheme: " + compScheme);
        } else {
            compLen = uncompLen = -1;
        }
        final int storedChunks = byteBuffer.getInt();

        if (compLen != -1) {
            final byte[] bytes = new byte[compLen];
            byteBuffer.get(bytes);
            byteBuffer = ByteBuffer.wrap(Zstd.decompress(bytes, uncompLen));
        }

        // Chunks
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
        final byte[] entityNbt = byteBuffer.get() > 0
                ? this.readCompressed(byteBuffer) : null;
        final byte[] tileEntityNbt = byteBuffer.get() > 0
                ? this.readCompressed(byteBuffer) : null;

        return new HoneyChunk(HoneyChunk.ChunkId.of(x, z), sectionMap, entityNbt, tileEntityNbt);
    }

}
