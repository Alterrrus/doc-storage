package org.voidst.docstorage.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.voidst.docstorage.controller.DocumentController;
import org.voidst.docstorage.dto.DocumentVResponse;

@Component
public class DocumentVRepresentationModelAssembler implements RepresentationModelAssembler<DocumentVResponse, EntityModel<DocumentVResponse>> {

  @Override
  public EntityModel<DocumentVResponse> toModel(DocumentVResponse entity) {
    return EntityModel.of(entity,
        linkTo(methodOn(DocumentController.class).getLazyDocument(entity.getId())).withSelfRel());

  }
}
