package org.voidst.docstorage.repo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.voidst.docstorage.domain.Chapter;
@Repository
public interface ChapterRepo extends MongoRepository<Chapter,String> {

  Chapter findByIdAndDocumentId(String id, String documentId);
  List<Chapter> findAllChapterByDocumentId(String documentId);
}
