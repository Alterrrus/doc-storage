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
/*    if (entity.getChapterIds() != null) {
      List<Link> linksList = entity.getChapterIds().stream().map(chapterId -> linkTo(methodOn(DocumentController.class).getChapter(entity.getId(), chapterId)).withSelfRel())
          .collect(Collectors.toList());
      linksList.add(0, linkTo(methodOn(DocumentController.class).getLazyDocument(entity.getId())).withSelfRel());
      Link[] links = linksList.toArray(new Link[0]);
      return EntityModel.of(entity, links);
    }*/
    return EntityModel.of(entity, linkTo(methodOn(DocumentController.class).getLazyDocument(entity.getId())).withSelfRel());
  }
}
