cd %~dp0

echo Starting Kafka Monitor...
start "Kafka Monitor" java -classpath "lib/*" org.kik.kafka.monitor.Application
