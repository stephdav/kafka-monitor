cd %~dp0

echo Starting Kafka Monitor...
start "Kafka Monitor" java -classpath "ext/;lib/*" org.kik.kafka.monitor.Application
