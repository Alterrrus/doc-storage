package org.voidst.docstorage.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.voidst.docstorage.controller.DocumentController;
import org.voidst.docstorage.dto.ChapterResponse;
@Component
public class ChapterRepresentationModelAssembler implements RepresentationModelAssembler<ChapterResponse, EntityModel<ChapterResponse>> {

  @Override
  public EntityModel<ChapterResponse> toModel(ChapterResponse entity) {
    return EntityModel.of(entity,
        linkTo(methodOn(DocumentController.class).getChapter(entity.getDocumentId(), entity.getId())).withSelfRel());

  }
}

