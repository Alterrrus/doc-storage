package org.voidst.docstorage.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.voidst.docstorage.domain.Chapter;
import org.voidst.docstorage.domain.DocumentV;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;
import org.voidst.docstorage.service.DocumentVService;

@RestController
@RequestMapping(value = "/doc")
public class DocumentController {

  private DocumentVService documentVServiceImpl;

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  public DocumentV createDocument(@RequestBody DocumentVRequest requestBody, HttpServletResponse response, HttpServletRequest request) {
    DocumentV documentV = documentVServiceImpl.createDocumentV(requestBody);
    response.setHeader("Location", request.getRequestURI() + "/" + documentV.getId());
    return documentV;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST, path = "/{documentId}")
  public Chapter createChapter(@PathVariable String documentId, @RequestBody ChapterRequest chapterRequest, HttpServletResponse response, HttpServletRequest request){
    Chapter chapter = documentVServiceImpl.createChapter(documentId,chapterRequest);
    response.setHeader("Location", request.getRequestURI() + "/" + chapter.getId());
    return chapter;
  }

  @RequestMapping(method = RequestMethod.GET)
  List<DocumentVResponse> getAllDocuments() {
    return documentVServiceImpl.findAllDocumentV();
  }


  @Autowired
  public void setDocumentVServiceImpl(DocumentVService documentVServiceImpl) {
    this.documentVServiceImpl = documentVServiceImpl;
  }
}
