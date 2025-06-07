#!/bin/bash
set -e

PLATFORMS="linux/amd64,linux/arm64"

build_and_push() {
  SERVICE_NAME=$1
  SERVICE_DIR=$2
  IMAGE="ghcr.io/funixproductions/funixgaming-api-${SERVICE_NAME}:${IMAGE_TAG}"

  echo "➡️ Building $SERVICE_NAME from $SERVICE_DIR"

  docker buildx build \
    --platform $PLATFORMS \
    --build-arg service_name=$SERVICE_NAME \
    --build-arg service_base_dir=$SERVICE_DIR \
    --tag $IMAGE \
    --push \
    .
}

# Liste de tes microservices
build_and_push "funixbot" "funixbot"
build_and_push "twitch" "twitch"
