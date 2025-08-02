package com.md.sqsratelimiter.limiter;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class ListenerManager {

    private final SqsAsyncClient client;
    private final ValidaLiberacaoMensagemUseCase useCase;
    private final AtomicReference<SqsMessageListenerContainer<MensagemLiberacaoDto>> containerRef = new AtomicReference<>();

    public ListenerManager(SqsAsyncClient client, FeatureToggleService toggleService, ValidaLiberacaoMensagemUseCase useCase) {
        this.client = client;
        this.useCase = useCase;
        createContainer(toggleService.getConfig());
    }

    private void createContainer(RateLimitConfigDto config) {
        SqsMessageListenerContainer<MensagemLiberacaoDto> old = containerRef.get();
        if (old != null) old.stop();

        SqsMessageListenerContainerFactory<MensagemLiberacaoDto> factory =
                SqsMessageListenerContainerFactory.<MensagemLiberacaoDto>builder()
                        .configure(opts -> opts
                                .maxConcurrentMessages(config.maxConcurrentMessages())
                                .maxMessagesPerPoll(config.maxMessagesPerPoll())
                        )
                        .sqsAsyncClient(client)
                        .build();

        SqsMessageListenerContainer<MensagemLiberacaoDto> newContainer =
                factory.createContainer("my-local-queue", MensagemLiberacaoDto.class, useCase);

        newContainer.start();
        containerRef.set(newContainer);
    }

    @EventListener
    public void onConfigChange(RateLimitChangedEventDto event) {
        createContainer(event.config());
    }
}
