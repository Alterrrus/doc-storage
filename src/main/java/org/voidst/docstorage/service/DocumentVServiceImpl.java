package org.voidst.docstorage.service;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.ChapterResponse;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;
import org.voidst.docstorage.dto.DtoMapper;
import org.voidst.docstorage.repo.ChapterRepo;
import org.voidst.docstorage.repo.DocumentVRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVServiceImpl implements DocumentVService {

  private final ChapterRepo chapterRepo;
  private final DocumentVRepo documentVRepo;
  private DtoMapper dtoMapper;

  @Override
  public DocumentVResponse createDocumentV(DocumentVRequest request) {
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
    if (doc != null) {
      return dtoMapper.getDocumentVResponse(doc);
    }
    return null;
  }

  @Override
  @Transactional
  public DocumentVResponse updateDocumentV(String documentId, DocumentVRequest request) {
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
      if (doc != null) {
        return dtoMapper.getDocumentVResponse(doc);
      }
    }
    return null;
  }

  @Override
  public ChapterResponse updateChapter(String documentId, String chapterId, ChapterRequest chapterRequest) {
    Chapter chapter = Chapter.builder()
        .documentId(documentId)
        .id(chapterId)
        .build();
    if (chapterRequest.getChapterTitle() != null) {
      chapter.setChapterTitle(chapterRequest.getChapterTitle());
    }
    if (chapterRequest.getContent() != null) {
      chapter.setContent(chapterRequest.getContent());
    }
    Chapter ch = null;
    try {
      if (chapter != null) {
        ch = chapterRepo.save(chapter);
      }
    } catch (Exception e) {
      log.error("", e);
    }
    if (ch != null) {
      return dtoMapper.getChapterResponse(ch);
    }
    return null;
  }

  @Override
  @Transactional
  public ChapterResponse createChapter(String documentId, ChapterRequest chapterRequest) {
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
    } catch (Exception e) {
      log.error("", e);
    }
    Chapter result = chapter.get();
    if (result != null) {
      return dtoMapper.getChapterResponse(result);
    }
    return null;
  }


  @Override
  public List<DocumentVResponse> findAllDocumentLazy() {
    return documentVRepo.findAll().stream()
        .map(doc -> dtoMapper.getDocumentVResponseLazy(doc)).collect(Collectors.toList());
  }

  @Override
  //@Cacheable(value = "document", key = "#documentId")
  public DocumentVResponse findById(String documentId) {
    Optional<DocumentV> document = documentVRepo.findById(documentId);
    return document.map(documentV -> dtoMapper.getDocumentVResponseLazy(documentV)).orElse(null);
  }

  @Override
  public List<ChapterResponse> findAllChapterByDocumentId(String documentId) {
    List<Chapter> list = chapterRepo.findAllChapterByDocumentId(documentId);
    if (list != null && !list.isEmpty()) {
      return list.stream().map(chapter -> dtoMapper.getChapterResponse(chapter)).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  @Override
  public ChapterResponse findChapterByIdAndDocumentId(String chapterId, String documentId) {
    Chapter chapter = chapterRepo.findByIdAndDocumentId(chapterId, documentId);
    return dtoMapper.getChapterResponse(chapter);
  }

  @Autowired
  public void setDtoMapper(DtoMapper dtoMapper) {
    this.dtoMapper = dtoMapper;
  }
}
