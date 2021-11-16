#!/usr/bin/env bash

VALID="true"

if [[ -z "${KAFKA_BROKERS}" ]]; then
  echo "Must set KAFKA_BROKERS environment variable"
  VALID="false"
fi

if [[ -z "${OAUTH_CLIENT_ID}" ]]; then
  echo "Must set OAUTH_CLIENT_ID environment variable"
  VALID="false"
fi

if [[ -z "${OAUTH_CLIENT_SECRET}" ]]; then
  echo "Must set OAUTH_CLIENT_SECRET environment variable"
  VALID="false"
fi

if [[ -z "${OAUTH_TOKEN_ENDPOINT_URI}" ]]; then
  echo "Must set OAUTH_TOKEN_ENDPOINT_URI environment variable"
  VALID="false"
fi

if [[ "${VALID}" == "false" ]]; then
  exit 1
fi

kubectl delete secret camel-quarkus-kafka-demo-secret

kubectl create secret generic camel-quarkus-kafka-demo-secret \
  --from-literal=KAFKA_BROKERS="${KAFKA_BROKERS}" \
  --from-literal=OAUTH_CLIENT_ID="${OAUTH_CLIENT_ID}" \
  --from-literal=OAUTH_CLIENT_SECRET="${OAUTH_CLIENT_SECRET}" \
  --from-literal=OAUTH_TOKEN_ENDPOINT_URI="${OAUTH_TOKEN_ENDPOINT_URI}"

kubectl label secret camel-quarkus-kafka-demo-secret "app.kubernetes.io/name"="camel-quarkus-kafka-demo"
