package gock.learning.books.messagedispatch;

import gock.learning.books.persistence.entity.Book;

public interface IDispatchMessageService {
  void bookCreated(Book book);
  void bookUpdated(Book book);
  void bookDeleted(Book book);
}
