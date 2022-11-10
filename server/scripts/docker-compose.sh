#!/bin/bash

env_value=$1
mode_value=$2

dockerComposeTask() {
  command=$1

  echo "Executing docker compose command=$command"
  docker compose --file ../docker-compose.yml --env-file ../config/.env."$env_value" "$command"
}

gradlewTask() {
  project=$1
  taskName=$2

  echo "Executing $taskName in project $project"
  cd ../"$project" || exit
  ./gradlew "$taskName"
  cd - || exit
}

if [[ $mode_value == "up" ]];
then
  gradlewTask application buildFatJar
  gradlewTask kgit shadow
  dockerComposeTask build
  dockerComposeTask up
else
  dockerComposeTask down
fi
