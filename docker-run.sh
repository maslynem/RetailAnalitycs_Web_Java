#!/bin/bash
sh Docker/clean.sh
rm -rf Docker/*/*.jar
mvn -N install
mvn clean -DskipTests package
cp src/RetailAnalytics_REST_API/target/*.jar Docker/RetailAnalyticsRestApi/CalculatorApi.jar
cp src/RetailAnalytics_Web/target/*.jar Docker/WebServer/CreditCalculatorAPI.jar
cp src/DiscoveryServer/target/*.jar Docker/DiscoveryServer/DiscoveryServer.jar
cd Docker && docker-compose up