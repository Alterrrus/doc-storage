package org.voidst.docstorage.dto;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.repo.ChapterRepo;

@Component
public class DtoMapper {

  private ChapterRepo chapterRepo;
  public ChapterResponse getChapterResponse(Chapter chapter) {
    return ChapterResponse.builder()
        .id(chapter.getId())
        .chapterTitle(chapter.getChapterTitle())
        .content(chapter.getContent())
        .documentId(chapter.getDocumentId())
        .build();
  }

  public DocumentVResponse getDocumentVResponse(DocumentV document){
    return DocumentVResponse.builder()
        .id(document.getId())
        .author(document.getAuthor())
        .title(document.getTitle())
        .chapterIds(document.getChaptersIds())
        .description(document.getDescription())
        .build();
  }

  public DocumentVResponse getDocumentVResponseLazy(DocumentV documentV) {
    return DocumentVResponse.builder()
        .id(documentV.getId())
        .author(documentV.getAuthor())
        .title(documentV.getTitle())
        .description(documentV.getDescription())
        .chapterIds(getChapterResponsesLazy(documentV))
        .build();
  }

  public DocumentVResponse getDocumentVResponseEager(DocumentV documentV) {
    return DocumentVResponse.builder()
        .id(documentV.getId())
        .author(documentV.getAuthor())
        .title(documentV.getTitle())
        .description(documentV.getDescription())
        .chapterIds(getChapterResponsesLazy(documentV))
        .chapters(getChapterResponsesEager(documentV))
        .build();
  }

  private Set<String> getChapterResponsesLazy(DocumentV documentV) {
    return documentV.getChaptersIds() != null ?
        documentV.getChaptersIds()
        : Collections.emptySet();
  }

  private Set<ChapterResponse> getChapterResponsesEager(DocumentV documentV) {
    if (documentV.getChaptersIds() != null) {
      return documentV.getChaptersIds().stream()
          .map(chapterRepo::findById)
          .filter(Optional::isPresent).map(Optional::get)
          .map(a -> ChapterResponse.builder()
              .id(a.getId())
              .chapterTitle(a.getChapterTitle())
              .content(a.getContent())
              .build())
          .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }
}
