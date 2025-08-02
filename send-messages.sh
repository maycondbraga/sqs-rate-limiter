#!/bin/bash

QUEUE_URL=$(aws sqs get-queue-url \
  --queue-name limited-queue \
  --endpoint-url http://localhost:4566 \
  --region us-east-1 \
  --output text)

echo "Enviando 1000 mensagens para $QUEUE_URL..."

for batch in $(seq 0 99); do
  entries=()
  for i in $(seq 1 10); do
    msg_num=$((batch * 10 + i))
    if [ $msg_num -le 1000 ]; then
      entries+=("Id=msg$msg_num,MessageBody=\"Mensagem número $msg_num\"")
    fi
  done
  aws sqs send-message-batch \
    --queue-url "$QUEUE_URL" \
    --endpoint-url http://localhost:4566 \
    --region us-east-1 \
    --entries "${entries[@]}" \
    > /dev/null
  echo "Lote $((batch + 1)) enviado"
done

echo "✅ Todas as mensagens foram enviadas com sucesso."