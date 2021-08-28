package net.valiry.honey.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import net.valiry.honey.HoneyChunk;
import net.valiry.honey.HoneyWorld;

public class HoneyWriterV1 extends HoneyWriter {

    @Override
    public byte[] write(final HoneyWorld world) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Header - Magic + Version
        outputStream.write(ByteBuffer.allocate(2).putShort((short) 0xAFFE).array());
        outputStream.write(ByteBuffer.allocate(2).putShort((short) 0x0001).array());

        final List<HoneyChunk> nonEmptyChunks = world.getChunks().stream()
                .filter(chunk -> chunk.getSectionMap().values().stream()
                        .anyMatch(section -> !section.isEmpty()))
                .collect(Collectors.toList());

        // Header - Chunk amount
        outputStream.write(ByteBuffer.allocate(4).putInt(nonEmptyChunks.size()).array());

        // Write chunks
        for (final HoneyChunk chunk : nonEmptyChunks) {
            this.writeChunk(outputStream, chunk);
        }

        // Write meta
        outputStream.write(world.getMetadataNbt() != null ? 1 : 0);
        if (world.getMetadataNbt() != null) {
            this.writeCompressed(outputStream, world.getMetadataNbt());
        }

        return outputStream.toByteArray();
    }

    private void writeChunk(final OutputStream outputStream, final HoneyChunk chunk) throws IOException {
        // Make section bitset
        final BitSet populatedSectionSet = new BitSet(16);
        chunk.getSectionMap().forEach((integer, section) -> {
            if (section.isEmpty()) {
                return;
            }
            populatedSectionSet.set(integer);
        });

        // Don't waste space if chunk is empty
        if (populatedSectionSet.isEmpty()) {
            return;
        }

        // Chunk header - X + Z + section bitset
        outputStream.write(ByteBuffer.allocate(4).putInt(chunk.getId().getX()).array());
        outputStream.write(ByteBuffer.allocate(4).putInt(chunk.getId().getZ()).array());
        outputStream.write(populatedSectionSet.toByteArray());

        // Entities
        outputStream.write(chunk.hasEntities() ? 1 : 0);
        if (chunk.hasEntities()) {
            this.writeCompressed(outputStream, chunk.getEntityNbt());
        }

        // Tile entities
        outputStream.write(chunk.hasTileEntities() ? 1 : 0);
        if (chunk.hasTileEntities()) {
            this.writeCompressed(outputStream, chunk.getTileEntityNbt());
        }
    }

}
