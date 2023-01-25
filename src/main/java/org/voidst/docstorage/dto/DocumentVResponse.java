package org.voidst.docstorage.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentVResponse {
  private String author;
  private String description;
  private String title;
  private List<ChapterResponse> chapters;
}