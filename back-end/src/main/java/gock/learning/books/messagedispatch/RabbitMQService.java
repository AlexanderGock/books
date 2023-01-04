package gock.learning.books.messagedispatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gock.learning.books.configuration.RabbitMQConfiguration;
import gock.learning.books.persistence.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "message.broker.implementation", havingValue = "rabbitmq")
@RequiredArgsConstructor
public class RabbitMQService implements IDispatchMessageService {

  private static final String ROUTING_KEY_BOOK_CREATE = "book.create";
  private static final String ROUTING_KEY_BOOK_UPDATE = "book.update";
  private static final String ROUTING_KEY_BOOK_DELETE = "book.delete";

  @Value(value = "${rabbitmq.book.topic.name}")
  public String topicExchangeName;

  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void bookCreated(Book book) {
    log.info("Publish create book message: {}", book);
    rabbitTemplate.convertAndSend(topicExchangeName, ROUTING_KEY_BOOK_CREATE, book);
    //sendMessageImpl(book, ROUTING_KEY_BOOK_CREATE);
  }

  @Override
  public void bookUpdated(Book book) {
    log.info("Publish update book message: {}", book);
    rabbitTemplate.convertAndSend(topicExchangeName, ROUTING_KEY_BOOK_UPDATE, book);
    //sendMessageImpl(book, ROUTING_KEY_BOOK_UPDATE);
  }

  @Override
  public void bookDeleted(Book book) {
    log.info("Publish delete book message: {}", book);
    rabbitTemplate.convertAndSend(topicExchangeName, ROUTING_KEY_BOOK_DELETE, book);
    //sendMessageImpl(book, ROUTING_KEY_BOOK_DELETE);
  }

  protected void sendMessageImpl(final Book book, final String routingKey) {
    try {
      String message = objectMapper.writeValueAsString(book);
      rabbitTemplate.convertAndSend(topicExchangeName, routingKey, message);
    } catch (JsonProcessingException e) {
      log.error("Error occurred when converting book object to message", e);
    }
  }
}
