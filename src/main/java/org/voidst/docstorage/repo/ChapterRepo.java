package org.voidst.docstorage.repo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.voidst.docstorage.domain.Chapter;

public interface ChapterRepo extends MongoRepository<Chapter,String> {

  Chapter findByIdAndDocumentId(String id, String documentId);
  List<Chapter> findAllChapterByDocumentId(String documentId);
}
