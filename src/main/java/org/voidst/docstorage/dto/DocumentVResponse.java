package org.voidst.docstorage.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentVResponse {
  private String id;
  private String author;
  private String description;
  private String title;
  private Set<String> chapterIds;
  private Set<ChapterResponse> chapters;
}
