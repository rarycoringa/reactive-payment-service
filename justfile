build:
    mvn clean package -DskipTests

run:
    mvn spring-boot:run \
        # -Dspring-boot.run.profiles=webflux

up:
    docker compose up --build --detach

down:
    docker compose down