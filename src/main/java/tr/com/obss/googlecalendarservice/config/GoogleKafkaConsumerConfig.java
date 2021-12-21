package tr.com.obss.googlecalendarservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import tr.com.common.config.CustomKafkaConsumerConfig;
import tr.com.common.dto.DomainMessage;

import java.util.HashMap;
//import tr.com.aselsan.ihtar.common.kafka.config.IhtarKafkaConsumerConfig;
//import tr.com.aselsan.ihtar.common.kafka.dto.IhtarMessageWrapper;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class GoogleKafkaConsumerConfig {
  private final CustomKafkaConsumerConfig kafkaConsumerConfig;
  private DomainMessage message;
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, DomainMessage> googleKafkaListenerContainerFactory() {
    return kafkaConsumerConfig.customKafkaListenerContainerFactory(new HashMap<>());
  }
}
