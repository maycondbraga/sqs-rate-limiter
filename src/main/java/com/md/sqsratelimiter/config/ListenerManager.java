package com.md.sqsratelimiter.config;

import com.md.sqsratelimiter.dto.MensagemLiberacaoDto;
import com.md.sqsratelimiter.dto.RateLimitChangedEventDto;
import com.md.sqsratelimiter.dto.RateLimitConfigDto;
import com.md.sqsratelimiter.usecase.ValidaLiberacaoMensagemUseCase;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import io.awspring.cloud.sqs.listener.acknowledgement.AcknowledgementCallback;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class ListenerManager {

    @Value("${sqs.queue.name}")
    private String sqsQueueName;

    private final SqsAsyncClient client;
    private final ValidaLiberacaoMensagemUseCase useCase;
    private final AtomicReference<SqsMessageListenerContainer<MensagemLiberacaoDto>> containerRef = new AtomicReference<>();

    public ListenerManager(SqsAsyncClient client, ValidaLiberacaoMensagemUseCase useCase) {
        this.client = client;
        this.useCase = useCase;
    }

    private void createContainer(RateLimitConfigDto config) {
        SqsMessageListenerContainer<MensagemLiberacaoDto> old = containerRef.get();
        if (old != null) old.stop();

        SqsMessageListenerContainerFactory<MensagemLiberacaoDto> factory =
                SqsMessageListenerContainerFactory.<MensagemLiberacaoDto>builder()
                        .configure(opts -> opts
                                .acknowledgementMode(AcknowledgementMode.MANUAL)
                                .maxConcurrentMessages(config.maxConcurrentMessages())
                                .maxMessagesPerPoll(config.maxMessagesPerPoll())
                        )
                        .sqsAsyncClient(client)
                        .build();

        SqsMessageListenerContainer<MensagemLiberacaoDto> newContainer = factory.createContainer(sqsQueueName);

        newContainer.setMessageListener(message -> {
            AcknowledgementCallback<MensagemLiberacaoDto> ack = (AcknowledgementCallback) message.getHeaders().get("AcknowledgementCallback");
            useCase.execute(message, ack);
        });

        newContainer.start();
        containerRef.set(newContainer);
    }

    @EventListener
    public void onConfigChange(RateLimitChangedEventDto event) {
        createContainer(event.config());
    }
}
