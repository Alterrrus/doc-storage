package org.voidst.docstorage.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.voidst.docstorage.domain.Chapter;

public interface ChapterRepo extends MongoRepository<Chapter,String> {

}
