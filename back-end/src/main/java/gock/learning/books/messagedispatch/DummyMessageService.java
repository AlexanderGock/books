package gock.learning.books.messagedispatch;

import gock.learning.books.persistence.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "message.broker.implementation", havingValue = "dummy", matchIfMissing = true)
public class DummyMessageService implements IDispatchMessageService {
  @Override
  public void bookCreated(Book book) {
    log.info("Create book: {}", book);
  }

  @Override
  public void bookUpdated(Book book) {
    log.info("Update book: {}", book);
  }

  @Override
  public void bookDeleted(Book book) {
    log.info("Delete book: {}", book);
  }
}
