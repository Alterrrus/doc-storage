package org.voidst.docstorage.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.voidst.docstorage.domain.DocumentV;

@Repository
public interface DocumentVRepo extends MongoRepository<DocumentV,String> {

  Page<DocumentV> findByAuthorAndTitle(String author, String title, Pageable pageable);

  Page<DocumentV> findByTitle(String title, Pageable pageable);

  Page<DocumentV> findByAuthor(String author, Pageable pageable);
}
