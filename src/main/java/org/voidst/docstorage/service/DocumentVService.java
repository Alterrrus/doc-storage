package org.voidst.docstorage.service;

import java.util.List;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;

public interface DocumentVService {
  DocumentV createDocumentV(DocumentVRequest request);
  Chapter createChapter(String documentId, ChapterRequest chapterRequest);
  List<DocumentVResponse> findAllDocumentV();

}
