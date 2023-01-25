package org.voidst.docstorage.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.voidst.docstorage.domain.DocumentV;

public interface DocumentVRepo extends MongoRepository<DocumentV,String> {

}
