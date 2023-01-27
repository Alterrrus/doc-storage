package org.voidst.docstorage.service;

import com.mongodb.MongoWriteException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    DocumentV doc = null;
    try {
      doc = documentVRepo.save(documentV);
    } catch (MongoWriteException e) {
      log.error("", e);
    }
    return doc;
  }

  @Override
  @Transactional
  public Chapter createChapter(String documentId, ChapterRequest chapterRequest) {
    Optional<DocumentV> documentV = documentVRepo.findById(documentId);
    AtomicReference<Chapter> chapter = new AtomicReference<>();
    try {
      documentV.ifPresent(document -> {
            chapter.set(Chapter.builder()
                .chapterTitle(chapterRequest.getChapterTitle())
                .content(chapterRequest.getContent())
                .documentId(document.getId())
                .build());
            chapterRepo.save(chapter.get());
            Set<String> set = document.getChaptersIds();
            if (set != null && !set.isEmpty()) {
              set.add(chapter.get().getId());
            } else {
              set = new LinkedHashSet<>();
              set.add(chapter.get().getId());
            }
            document.setChaptersIds(set);
            documentVRepo.save(document);
          }
      );
    } catch (MongoWriteException e) {
      log.error("", e);
    }
    return chapter.get();
  }


  @Override
  public List<DocumentVResponse> findAllDocumentLazy() {
    return documentVRepo.findAll().stream()
        .map(this::getDocumentVResponseLazy).collect(Collectors.toList());
  }

  @Override
  public List<DocumentVResponse> findAllDocumentEager() {
    return documentVRepo.findAll().stream()
        .map(this::getDocumentVResponseEager).collect(Collectors.toList());
  }

  @Override
  public List<Chapter> findAllChapters() {
    return null;
  }

  private DocumentVResponse getDocumentVResponseLazy(DocumentV documentV) {
    return DocumentVResponse.builder()
        .author(documentV.getAuthor())
        .title(documentV.getTitle())
        .description(documentV.getDescription())
        .chapterIds(getChapterResponsesLazy(documentV))
        .build();
  }

  private DocumentVResponse getDocumentVResponseEager(DocumentV documentV) {
    return DocumentVResponse.builder()
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
              .chapterTitle(a.getChapterTitle())
              .content(a.getContent())
              .build())
          .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }
}
