package org.voidst.docstorage.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.ChapterResponse;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;
import org.voidst.docstorage.repo.ChapterRepo;
import org.voidst.docstorage.repo.DocumentVRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVServiceImpl implements DocumentVService {

  private final ChapterRepo chapterRepo;
  private final DocumentVRepo documentVRepo;

  @Override
  public DocumentV createDocumentV(DocumentVRequest request) {
    DocumentV documentV = DocumentV.builder()
        .title(request.getTitle())
        .description(request.getDescription())
        .author(request.getAuthor())
        .build();
    return documentVRepo.save(documentV);
  }

  @Override
  @Transactional
  public Chapter createChapter(String documentId, ChapterRequest chapterRequest) {
    Optional<DocumentV> documentV = documentVRepo.findById(documentId);
    AtomicReference<Chapter> chapter = new AtomicReference<>();
    documentV.ifPresent(document -> {
          chapter.set(Chapter.builder()
              .chapterTitle(chapterRequest.getChapterTitle())
              .content(chapterRequest.getContent())
              .documentV(document)
              .build());
          chapterRepo.save(chapter.get());
        }
    );
    return chapter.get();
  }


  @Override
  public List<DocumentVResponse> findAllDocumentV() {

    return documentVRepo.findAll().stream()
        .map(this::getDocumentVResponse).collect(Collectors.toList());
  }

  private DocumentVResponse getDocumentVResponse(DocumentV documentV) {
    return DocumentVResponse.builder()
        .author(documentV.getAuthor())
        .title(documentV.getTitle())
        .description(documentV.getDescription())
        .chapters(getChapterResponses(documentV))
        .build();
  }

  private ChapterResponse getChapterResponse(Chapter chapter) {
    return ChapterResponse.builder()
        .chapterTitle(chapter.getChapterTitle())
        .content(chapter.getContent())
        .build();
  }

  private List<ChapterResponse> getChapterResponses(DocumentV documentV) {
    return documentV.getChapters() != null ?
        documentV.getChapters().stream().map(this::getChapterResponse).collect(Collectors.toList())
        : Collections.emptyList();
  }
}
