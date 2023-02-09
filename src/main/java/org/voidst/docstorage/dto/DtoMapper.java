package org.voidst.docstorage.dto;

import java.util.Collections;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;

@Component
public class DtoMapper {
  public ChapterResponse getChapterResponse(Chapter chapter) {
    return ChapterResponse.builder()
        .id(chapter.getId())
        .chapterTitle(chapter.getChapterTitle())
        .content(chapter.getContent())
        .documentId(chapter.getDocumentId())
        .build();
  }

  public DocumentVResponse getDocumentVResponseLazy(DocumentV documentV) {
    return DocumentVResponse.builder()
        .id(documentV.getId())
        .author(documentV.getAuthor())
        .title(documentV.getTitle())
        .chapterIds(getChapterResponsesLazy(documentV))
        .description(documentV.getDescription())
        .build();
  }

  private Set<String> getChapterResponsesLazy(DocumentV documentV) {
    return documentV.getChaptersIds() != null ?
        documentV.getChaptersIds()
        : Collections.emptySet();
  }
}
