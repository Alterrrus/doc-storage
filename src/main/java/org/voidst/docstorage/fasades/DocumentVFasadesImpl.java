package org.voidst.docstorage.fasades;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.ChapterResponse;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;
import org.voidst.docstorage.dto.DtoMapper;
import org.voidst.docstorage.service.doc_storage.DocumentVService;

@Component
public class DocumentVFasadesImpl implements DocumentVFasades {

  private final DocumentVService documentVServiceImpl;

  private final DtoMapper dtoMapper;

  @Autowired
  public DocumentVFasadesImpl(DocumentVService documentVServiceImpl, DtoMapper dtoMapper) {
    this.documentVServiceImpl = documentVServiceImpl;
    this.dtoMapper = dtoMapper;
  }

  @Override
  public DocumentVResponse createDocumentV(DocumentVRequest request) {
    DocumentV doc = documentVServiceImpl.createDocumentV(request);
    return doc != null ? dtoMapper.getDocumentVResponseLazy(doc) : null;
  }

  @Override
  public ChapterResponse createChapter(String documentId, ChapterRequest chapterRequest) {
    Chapter result = documentVServiceImpl.createChapter(documentId, chapterRequest);
    return result != null ? dtoMapper.getChapterResponse(result) : null;
  }

  @Override
  public DocumentVResponse updateDocumentV(String documentId, DocumentVRequest request) {
    DocumentV doc = documentVServiceImpl.updateDocumentV(documentId, request);
    return doc != null ? dtoMapper.getDocumentVResponseLazy(doc) : null;
  }

  @Override
  public ChapterResponse updateChapter(String documentId, String chapterId, ChapterRequest chapterRequest) {
    Chapter ch = documentVServiceImpl.updateChapter(documentId, chapterId, chapterRequest);
    return ch != null ? dtoMapper.getChapterResponse(ch) : null;
  }

  @Override
  public DocumentVResponse findDocumentById(String documentId) {
    Optional<DocumentV> document = documentVServiceImpl.findDocumentById(documentId);
    return document.map(dtoMapper::getDocumentVResponseLazy).orElse(null);
  }

  @Override
  public List<ChapterResponse> findAllChapterByDocumentId(String documentId) {
    List<Chapter> list = documentVServiceImpl.findAllChapterByDocumentId(documentId);
    if (CollectionUtils.isNotEmpty(list)) {
      return list.stream().map(dtoMapper::getChapterResponse).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  @Override
  public ChapterResponse findChapterByIdAndDocumentId(String chapterId, String documentId) {
    Chapter chapter = documentVServiceImpl.findChapterByIdAndDocumentId(chapterId, documentId);
    return chapter != null ? dtoMapper.getChapterResponse(chapter) : null;
  }

  @Override
  public Page<DocumentVResponse> findDocumentsByParameter(int page, int size, String author, String title) {
    Page<DocumentV> result = documentVServiceImpl.findDocumentsByParameter(page, size, author, title);
    return result.map(dtoMapper::getDocumentVResponseLazy);
  }
}
