package org.voidst.docstorage.service.doc_storage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.DocumentVRequest;
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
    } catch (Exception e) {
      log.error("", e);
    }
    return doc;
  }

  @Override
  @Transactional
  public DocumentV updateDocumentV(String documentId, DocumentVRequest request) {
    Optional<DocumentV> findDocument = documentVRepo.findById(documentId);
    if (findDocument.isPresent()) {
      DocumentV temp = findDocument.get();
      if (request.getAuthor() != null) {
        temp.setAuthor(request.getAuthor());
      }
      if (request.getTitle() != null) {
        temp.setTitle(request.getTitle());
      }
      if (request.getDescription() != null) {
        temp.setDescription(request.getDescription());
      }
      DocumentV doc = null;
      try {
        doc = documentVRepo.save(temp);
      } catch (Exception e) {
        log.error("", e);
      }
      return doc;
    }
    return null;
  }

  @Override
  public Chapter updateChapter(String documentId, String chapterId, ChapterRequest chapterRequest) {
    Optional<Chapter> findChapter = chapterRepo.findById(chapterId);
    if (findChapter.isPresent()) {
      Chapter temp = findChapter.get();
      if (chapterRequest.getChapterTitle() != null) {
        temp.setChapterTitle(chapterRequest.getChapterTitle());
      }
      if (chapterRequest.getContent() != null) {
        temp.setContent(chapterRequest.getContent());
      }
      Chapter ch = null;
      try {
        ch = chapterRepo.save(temp);
      } catch (Exception e) {
        log.error("", e);
      }
      return ch;
    }
    return null;
  }

  @Override
  @Transactional
  public Chapter createChapter(String documentId, ChapterRequest chapterRequest) {
    Optional<DocumentV> documentV = documentVRepo.findById(documentId);
    AtomicReference<Chapter> chapter = new AtomicReference<>();
    AtomicReference<Chapter> savedChapter = new AtomicReference<>();
    try {
      documentV.ifPresent(document -> {
            chapter.set(Chapter.builder()
                .chapterTitle(chapterRequest.getChapterTitle())
                .content(chapterRequest.getContent())
                .documentId(document.getId())
                .build());
            savedChapter.set(chapterRepo.save(chapter.get()));
            Set<String> set = document.getChaptersIds();
            if (CollectionUtils.isNotEmpty(set)) {
              set.add(savedChapter.get().getId());
            } else {
              set = new LinkedHashSet<>();
              set.add(savedChapter.get().getId());
            }
            document.setChaptersIds(set);
            documentVRepo.save(document);
          }
      );
    } catch (Exception e) {
      log.error("", e);
    }
    return savedChapter.get();
  }

  @Override
  public Page<DocumentV> findDocumentsByParameter(int page, int size, String author, String title) {
    Pageable pageable = PageRequest.of(page, size);
    if (author != null && title != null) {
      return documentVRepo.findByAuthorAndTitle(author, title, pageable);
    }
    if (title != null) {
      return documentVRepo.findByTitle(title, pageable);
    }
    if (author != null) {
      return documentVRepo.findByAuthor(author, pageable);
    }
    return documentVRepo.findAll(pageable);
  }

  @Override
  //@Cacheable(value = "document", key = "#documentId")
  public Optional<DocumentV> findDocumentById(String documentId) {
    return documentVRepo.findById(documentId);
  }

  @Override
  public List<Chapter> findAllChapterByDocumentId(String documentId) {
    return chapterRepo.findAllChapterByDocumentId(documentId);
  }

  @Override
  public Chapter findChapterByIdAndDocumentId(String chapterId, String documentId) {
    return chapterRepo.findByIdAndDocumentId(chapterId, documentId);
  }
}
