package org.voidst.docstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "chapter")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Chapter {

  @MongoId
  private String id;
  //@DocumentReference(lazy = true)
  @DBRef
  private DocumentV documentV;
  private String chapterTitle;
  private String content;

}
