package org.ohorodnik.videostreaming.utils.records;

import org.ohorodnik.videostreaming.dto.PlayingVideoDto;

public record ChunkWithMetadata(
        PlayingVideoDto playingVideoDto,
        byte[] chunk
) {
}
