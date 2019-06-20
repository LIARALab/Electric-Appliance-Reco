FROM arm32v7/gradle:5.4-jdk11-slim as builder

COPY --chown=gradle:gradle ./app /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM arm32v7/openjdk:11-jdk-slim

RUN mkdir -p /usr/app/JSON_Signatures
COPY ./JSON_Signatures /usr/app/JSON_Signatures

RUN mkdir -p /usr/app/JSON_Signatures_Recommendation
COPY ./JSON_Signatures_Recommendation /usr/app/JSON_Signatures_Recommendation

COPY --from=builder /home/gradle/src/build/distributions/electric-1.0.tar /usr/app
WORKDIR /usr/app
RUN tar -xvf electric-1.0.tar
CMD ["./electric-1.0/bin/electric", "./JSON_Signatures", "./JSON_Signatures_Recommendation"]
