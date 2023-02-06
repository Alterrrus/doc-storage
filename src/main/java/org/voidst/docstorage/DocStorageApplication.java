package org.voidst.docstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.voidst.docstorage.dto.DocumentVResponse;


@SpringBootApplication
@EnableCaching
@EnableMongoRepositories
@EnableSpringDataWebSupport
public class DocStorageApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocStorageApplication.class, args);
  }



}
