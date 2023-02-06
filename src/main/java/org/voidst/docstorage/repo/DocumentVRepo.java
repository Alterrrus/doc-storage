package org.voidst.docstorage.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.voidst.docstorage.domain.DocumentV;
@Repository
public interface DocumentVRepo extends MongoRepository<DocumentV,String> {

}
