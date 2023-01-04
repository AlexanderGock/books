package gock.learning.books.persistence.eventhandler;

import gock.learning.books.messagedispatch.IDispatchMessageService;
import gock.learning.books.persistence.entity.Book;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RepositoryEventHandler
@Slf4j
@Component
@RequiredArgsConstructor
public class BookEventHandler {

  protected final IDispatchMessageService dispatchMessageService;

  @HandleAfterCreate
  public void handleBookAfterCreate(Book book) {
    log.info("Book {} is created", book.getId());
    dispatchMessageService.bookCreated(book);
  }

  @HandleAfterSave
  public void handleBookAfterSave(Book book) {
    log.info("Book {} is saved", book.getId());
    dispatchMessageService.bookUpdated(book);
  }

  @HandleAfterDelete
  public void handleBookAfterDelete(Book book) {
    log.info("Book {} is deleted", book.getId());
    dispatchMessageService.bookDeleted(book);
  }

}
