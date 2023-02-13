package org.voidst.docstorage.dto;

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
public class ChapterResponse {
  private String id;
  private String chapterTitle;
  private String content;
  private String documentId;
}
