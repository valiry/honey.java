package net.valiry.honey;

import java.util.Map;
import java.util.Objects;

public class HoneyChunk {

    private final ChunkId id;
    private final Map<Integer, HoneyChunkSection> sectionMap;
    private final byte[] entityNbt;
    private final byte[] tileEntityNbt;

    public HoneyChunk(final ChunkId id,
                      final Map<Integer, HoneyChunkSection> sectionMap,
                      final byte[] entityNbt,
                      final byte[] tileEntityNbt) {
        this.id = id;
        this.sectionMap = sectionMap;
        this.entityNbt = entityNbt;
        this.tileEntityNbt = tileEntityNbt;
    }

    public boolean hasEntities() {
        return this.entityNbt != null;
    }

    public boolean hasTileEntities() {
        return this.tileEntityNbt != null;
    }

    public ChunkId getId() {
        return this.id;
    }

    public byte[] getEntityNbt() {
        return this.entityNbt;
    }

    public byte[] getTileEntityNbt() {
        return this.tileEntityNbt;
    }

    public Map<Integer, HoneyChunkSection> getSectionMap() {
        return this.sectionMap;
    }

    public static class HoneyChunkSection {

        private final short[] states;

        public HoneyChunkSection(final short[] states) {
            this.states = states;
        }

        public boolean isEmpty() {
            // Is there a better way of doing this?
            for (final short state : this.states) {
                if (state != 0) {
                    return false;
                }
            }
            return true;
        }

        public short[] getStates() {
            return this.states;
        }

    }

    public static class ChunkId {

        private final int x;
        private final int z;

        private ChunkId(final int x, final int z) {
            this.x = x;
            this.z = z;
        }

        public static ChunkId of(final int x, final int z) {
            return new ChunkId(x, z);
        }

        public int getX() {
            return this.x;
        }

        public int getZ() {
            return this.z;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ChunkId chunkId = (ChunkId) o;
            return this.x == chunkId.x && this.z == chunkId.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.z);
        }

    }

}
