package org.voidst.docstorage.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@Slf4j
@RequestMapping(value = "/doc")
public class DocumentController {

  private DocumentVService documentVServiceImpl;
  private DocumentVRepresentationModelAssembler documentVRepresentationModelAssembler;
  private ChapterRepresentationModelAssembler chapterRepresentationModelAssembler;
  private PagedResourcesAssembler<DocumentVResponse> pagedResourcesAssembler;

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)

  public EntityModel<DocumentVResponse> createDocument(@RequestBody DocumentVRequest requestBody) {
    DocumentVResponse document = documentVServiceImpl.createDocumentV(requestBody);
    return documentVRepresentationModelAssembler.toModel(document);
  }

  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(method = RequestMethod.PUT, path = "/{documentId}")
  public EntityModel<DocumentVResponse> updateDocument(@PathVariable String documentId, @RequestBody DocumentVRequest requestBody) {
    DocumentVResponse document = documentVServiceImpl.updateDocumentV(documentId, requestBody);
    return documentVRepresentationModelAssembler.toModel(document);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST, path = "/{documentId}")
  public EntityModel<ChapterResponse> createChapter(@PathVariable String documentId, @RequestBody ChapterRequest chapterRequest) {
    ChapterResponse chapter = documentVServiceImpl.createChapter(documentId, chapterRequest);
    return chapterRepresentationModelAssembler.toModel(chapter);
  }

  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(method = RequestMethod.PUT, path = "/{documentId}/chapters/{chapterId}")
  public EntityModel<ChapterResponse> updateChapter(@PathVariable String documentId, @PathVariable String chapterId, @RequestBody ChapterRequest chapterRequest) {
    ChapterResponse chapter = documentVServiceImpl.updateChapter(documentId, chapterId, chapterRequest);
    return chapterRepresentationModelAssembler.toModel(chapter);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/{documentId}")
  public EntityModel<DocumentVResponse> getLazyDocument(@PathVariable String documentId) {
    DocumentVResponse document = documentVServiceImpl.findDocumentById(documentId);
    return documentVRepresentationModelAssembler.toModel(document);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/{documentId}/chapters/{chapterId}")
  public EntityModel<ChapterResponse> getChapter(@PathVariable String documentId, @PathVariable String chapterId) {
    ChapterResponse chapter = documentVServiceImpl.findChapterByIdAndDocumentId(chapterId, documentId);
    return chapterRepresentationModelAssembler.toModel(chapter);
  }

  @RequestMapping(method = RequestMethod.GET)
  @CrossOrigin(origins = "http://localhost:3000")
  CollectionModel<EntityModel<DocumentVResponse>> getAllDocuments(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String author,
      @RequestParam(required = false) String title) {
    Page<DocumentVResponse> list = documentVServiceImpl.findDocumentsByParameter(page, size, author, title);
    log.info(String.format("invoke localhost getAllDocuments size: %s", list.getTotalPages()));
    return pagedResourcesAssembler.toModel(list, documentVRepresentationModelAssembler);
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

  @Autowired
  public void setPagedResourcesAssembler(PagedResourcesAssembler<DocumentVResponse> pagedResourcesAssembler) {
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }
}
