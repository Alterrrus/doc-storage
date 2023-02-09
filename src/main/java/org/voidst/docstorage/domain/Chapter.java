package org.voidst.docstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "chapter")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@CompoundIndex(unique = true,name = "documentId_chapterTitle", def = "{'documentId':1,'chapterTitle':1}")
@CompoundIndex(unique = true,name = "chapterTitle_documentId", def = "{'chapterTitle':1,'documentId':1}")
public class Chapter {

  @MongoId
  private String id;
  private String documentId;
  private String chapterTitle;
  private String content;

}
