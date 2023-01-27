package org.voidst.docstorage.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.voidst.docstorage.dto.ChapterRequest;
import org.voidst.docstorage.dto.ChapterResponse;
import org.voidst.docstorage.dto.DocumentVRequest;
import org.voidst.docstorage.dto.DocumentVResponse;
import org.voidst.docstorage.hateoas.ChapterRepresentationModelAssembler;
import org.voidst.docstorage.hateoas.DocumentVRepresentationModelAssembler;
import org.voidst.docstorage.service.DocumentVService;

@RestController
@RequestMapping(value = "/doc")
public class DocumentController {

  private DocumentVService documentVServiceImpl;
  private DocumentVRepresentationModelAssembler documentVRepresentationModelAssembler;
  private ChapterRepresentationModelAssembler chapterRepresentationModelAssembler;

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  public EntityModel<DocumentVResponse> createDocument(@RequestBody DocumentVRequest requestBody) {
    DocumentVResponse document = documentVServiceImpl.createDocumentV(requestBody);
    return documentVRepresentationModelAssembler.toModel(document);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST, path = "/{documentId}")
  public EntityModel<ChapterResponse> createChapter(@PathVariable String documentId, @RequestBody ChapterRequest chapterRequest, HttpServletResponse response,
      HttpServletRequest request) {
    ChapterResponse chapter = documentVServiceImpl.createChapter(documentId, chapterRequest);
    return chapterRepresentationModelAssembler.toModel(chapter);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/{documentId}")
  public EntityModel<DocumentVResponse> getLazyDocument(@PathVariable String documentId) {
    DocumentVResponse document = documentVServiceImpl.findById(documentId);
    return documentVRepresentationModelAssembler.toModel(document);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/{documentId}/chapters/{chapterId}")
  public EntityModel<ChapterResponse> getChapter(@PathVariable String documentId, @PathVariable String chapterId) {
    ChapterResponse chapter = documentVServiceImpl.findChapterByIdAndDocumentId(chapterId, documentId);
    return chapterRepresentationModelAssembler.toModel(chapter);
  }

  @RequestMapping(method = RequestMethod.GET)
  CollectionModel<EntityModel<DocumentVResponse>> getAllDocuments() {
    List<DocumentVResponse> list = documentVServiceImpl.findAllDocumentLazy();
    return documentVRepresentationModelAssembler.toCollectionModel(list);
  }

  @RequestMapping(value = "/{documentId}/chapters", method = RequestMethod.GET)
  CollectionModel<EntityModel<ChapterResponse>> getAllChaptersByDocumentId(@PathVariable String documentId) {
    List<ChapterResponse> list = documentVServiceImpl.findAllChapterByDocumentId(documentId);
    return chapterRepresentationModelAssembler.toCollectionModel(list);
  }

  @Autowired
  public void setDocumentVServiceImpl(DocumentVService documentVServiceImpl) {
    this.documentVServiceImpl = documentVServiceImpl;
  }

  @Autowired
  public void setDtoRepresentationModelAssembler(DocumentVRepresentationModelAssembler documentVRepresentationModelAssembler) {
    this.documentVRepresentationModelAssembler = documentVRepresentationModelAssembler;
  }

  @Autowired
  public void setChapterRepresentationModelAssembler(ChapterRepresentationModelAssembler chapterRepresentationModelAssembler) {
    this.chapterRepresentationModelAssembler = chapterRepresentationModelAssembler;
  }
}
