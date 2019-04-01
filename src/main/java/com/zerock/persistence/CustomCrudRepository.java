package com.zerock.persistence;

import com.zerock.domain.WebBoard;
import org.springframework.data.repository.CrudRepository;

public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard {
}
