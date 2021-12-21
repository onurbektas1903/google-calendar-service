package tr.com.obss.googlecalendarservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tr.com.common.dto.DomainMessage;


@Component
@RequiredArgsConstructor
public class GoogleKafkaMessageListener {
    @KafkaListener(
            topics = "${application-topics.google}",
            containerFactory = "customKafkaListenerContainerFactory")
    public void listenNotificationMessages(@Payload DomainMessage message){

    System.out.println(message);
    }
}
