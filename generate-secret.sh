#!/usr/bin/env bash

KAFKA_BROKERS=test
CLIENT_ID=srvc-acct-750d7986-9f79-48f7-ae66-21b5106716a9
CLIENT_SECRET=4d445d69-ed01-4c5f-b4bb-cc2717e09d15

kubectl delete secret camel-quarkus-kafka-mcs-secret

kubectl create secret generic camel-quarkus-kafka-mcs-secret \
  --from-literal=KAFKA_BROKERS="${KAFKA_BROKERS}" \
  --from-literal=OAUTH_CLIENT_ID="${CLIENT_ID}" \
  --from-literal=OAUTH_CLIENT_SECRET="${CLIENT_SECRET}"

kubectl label secret camel-quarkus-kafka-mcs-secret "app.kubernetes.io/name"="camel-quarkus-examples-kafka"

kubectl get secret camel-quarkus-kafka-mcs-secret -o yaml > src/main/kubernetes/openshift.yml

sed -i "/creationTimestamp/d" src/main/kubernetes/openshift.yml
sed -i "/namespace/d" src/main/kubernetes/openshift.yml
sed -i "/resourceVersion/d" src/main/kubernetes/openshift.yml
sed -i "/uid/d" src/main/kubernetes/openshift.yml
