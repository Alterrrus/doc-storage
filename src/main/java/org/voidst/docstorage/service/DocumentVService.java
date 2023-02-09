package org.voidst.docstorage.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.ChapterResponse;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;

public interface DocumentVService {
  DocumentVResponse createDocumentV(DocumentVRequest request);
  ChapterResponse createChapter(String documentId, ChapterRequest chapterRequest);

  DocumentVResponse findDocumentById(String documentId);

  ChapterResponse findChapterByIdAndDocumentId(String documentId, String ChapterId);

  List<ChapterResponse> findAllChapterByDocumentId(String documentId);
  DocumentVResponse updateDocumentV(String documentId, DocumentVRequest request);

  ChapterResponse updateChapter(String documentId, String chapterId, ChapterRequest chapterRequest);
  Page<DocumentVResponse> findDocumentsByParameter(int page, int size, String author, String title);
}
