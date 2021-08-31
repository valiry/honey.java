package net.valiry.honey;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HoneyWorld {

    private final Map<HoneyChunk.ChunkId, HoneyChunk> chunkMap = new HashMap<>();
    private int protocolVersion;
    private byte[] metadataNbt;

    public void put(final HoneyChunk chunk) {
        this.chunkMap.put(chunk.getId(), chunk);
    }

    public HoneyChunk getChunk(final int x, final int z) {
        return this.chunkMap.get(HoneyChunk.ChunkId.of(x, z));
    }

    public Collection<HoneyChunk> getChunks() {
        return this.chunkMap.values();
    }

    public byte[] getMetadataNbt() {
        return this.metadataNbt;
    }

    public void setMetadataNbt(final byte[] metadataNbt) {
        this.metadataNbt = metadataNbt;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

}
