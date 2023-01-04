package gock.learning.books.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "message.broker.implementation", havingValue = "rabbitmq")
@Slf4j
public class RabbitMQConfiguration {

  @Value(value = "${rabbitmq.book.topic.name}")
  public String topicExchangeName;
  @Value(value = "${rabbitmq.book.queue.names:}")
  public List<String> queueNames;

  @Bean
  public Declarables bookBindings() {
    final TopicExchange topicExchange = new TopicExchange(topicExchangeName);
    final Declarables declarables = new Declarables(topicExchange);

    if (!CollectionUtils.isEmpty(queueNames)) {
      queueNames.stream().forEach(queueName -> {
        Queue queue = new Queue(queueName, true);
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with("book.*");
        declarables.getDeclarables().add(queue);
        declarables.getDeclarables().add(binding);
      });
    }

    log.info("RabbitMQ is configured");
    return declarables;
  }

  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
    final var rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
    return rabbitTemplate;
  }

  @Bean
  public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
