package org.voidst.docstorage.domain;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "document")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@CompoundIndex(unique = true,name = "author_title", def = "{'author':1,'title':1}")
@CompoundIndex(unique = true,name = "title_author", def = "{'title':1,'author':1}")
public class DocumentV {

  @MongoId
  private String id;
  private String author;
  private String description;
  private String title;

  private Set<String> chaptersIds;


}
