package org.voidst.docstorage.service.doc_storage;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
      return dtoMapper.getDocumentVResponseLazy(doc);
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
        return dtoMapper.getDocumentVResponseLazy(doc);
      }
    }
    return null;
  }

  @Override
  public ChapterResponse updateChapter(String documentId, String chapterId, ChapterRequest chapterRequest) {
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
      if (ch != null) {
        return dtoMapper.getChapterResponse(ch);
      }
    }
    return null;
  }

  @Override
  @Transactional
  public ChapterResponse createChapter(String documentId, ChapterRequest chapterRequest) {
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
    Chapter result = savedChapter.get();
    if (result != null) {
      return dtoMapper.getChapterResponse(result);
    }
    return null;
  }

  @Override
  public Page<DocumentVResponse> findDocumentsByParameter(int page, int size, String author, String title) {
    Pageable pageable = PageRequest.of(page, size);
    if (author != null && title != null) {
      return documentVRepo.findByAuthorAndTitle(author, title, pageable).map(a -> dtoMapper.getDocumentVResponseLazy(a));
    }
    if (title != null) {
      return documentVRepo.findByTitle(title, pageable).map(a -> dtoMapper.getDocumentVResponseLazy(a));
    }
    if (author != null) {
      return documentVRepo.findByAuthor(author, pageable).map(a -> dtoMapper.getDocumentVResponseLazy(a));
    }
    return documentVRepo.findAll(pageable).map(a -> dtoMapper.getDocumentVResponseLazy(a));
  }

  @Override
  //@Cacheable(value = "document", key = "#documentId")
  public DocumentVResponse findDocumentById(String documentId) {
    Optional<DocumentV> document = documentVRepo.findById(documentId);
    return document.map(documentV -> dtoMapper.getDocumentVResponseLazy(documentV)).orElse(null);
  }

  @Override
  public List<ChapterResponse> findAllChapterByDocumentId(String documentId) {
    List<Chapter> list = chapterRepo.findAllChapterByDocumentId(documentId);
    if (CollectionUtils.isNotEmpty(list)) {
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