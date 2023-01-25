package org.voidst.docstorage.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "document")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentV {

  @MongoId
  private String id;
  @Indexed
  private String author;
  private String description;
  private String title;
/*  @ReadOnlyProperty
  @DocumentReference(lookup = "{'documentV':?#{#self._id}}")*/
  @DBRef
  private List<Chapter> chapters;


}
