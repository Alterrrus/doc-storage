package org.voidst.docstorage.service.doc_storage;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.DocumentVRequest;

public interface DocumentVService {

  DocumentV createDocumentV(DocumentVRequest request);

  Chapter createChapter(String documentId, ChapterRequest chapterRequest);

  Optional<DocumentV> findDocumentById(String documentId);

  Chapter findChapterByIdAndDocumentId(String documentId, String ChapterId);

  List<Chapter> findAllChapterByDocumentId(String documentId);

  DocumentV updateDocumentV(String documentId, DocumentVRequest request);

  Chapter updateChapter(String documentId, String chapterId, ChapterRequest chapterRequest);

  Page<DocumentV> findDocumentsByParameter(int page, int size, String author, String title);
}
