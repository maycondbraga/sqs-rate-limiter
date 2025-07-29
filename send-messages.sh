#!/bin/bash

QUEUE_URL=$(aws sqs get-queue-url \
  --queue-name limited-queue \
  --endpoint-url http://localhost:4566 \
  --region us-east-1 \
  --output text)

echo "Enviando 100 mensagens para $QUEUE_URL..."

for i in $(seq 1 100); do
  aws sqs send-message \
    --queue-url "$QUEUE_URL" \
    --message-body "Mensagem número $i" \
    --endpoint-url http://localhost:4566 \
    --region us-east-1 \
    > /dev/null
  echo "Mensagem $i enviada"
done

echo "✅ Todas as mensagens foram enviadas com sucesso."
