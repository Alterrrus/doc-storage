package org.voidst.docstorage.service;

import java.util.List;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.ChapterResponse;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;

public interface DocumentVService {
  DocumentVResponse createDocumentV(DocumentVRequest request);
  ChapterResponse createChapter(String documentId, ChapterRequest chapterRequest);
  List<DocumentVResponse> findAllDocumentLazy();

  DocumentVResponse findById(String documentId);

  ChapterResponse findChapterByIdAndDocumentId(String documentId, String ChapterId);

  List<ChapterResponse> findAllChapterByDocumentId(String documentId);
}
