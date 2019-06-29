# kafka-monitor
Monitor messages from kafka topics

## How to run application within Eclipse

1. *Import Existing Maven Project* in Eclipse
2. Set Kafka configuration in `application.properties`
  * **kafka.bootstrap-servers**
  * **kafka.consumer.topics**
3. Start Java application with <i>Main class</i> value `org.kik.kafka.monitor.Application`
4. Connect with your browser at http://localhost:4567/kafka-monitor/

## Deploy on Windows environment

* Execute `mvn clean package` and get archive `kafka-monitor-*.zip` in the `target` directory.
* On `<your-host>`, install appliation:

``` shell
cd <install directory>
unzip kafka-monitor-1.0.zip
kafka-monitor.bat
```
* Connect with your browser at http://your-host:4567/kafka-monitor/

## Deploy on Linux environment

* Execute `mvn clean package` and get archive `kafka-monitor-*.zip` in the `target` directory.
* On `<your-host>`, install appliation:

``` shell
unzip kafka-monitor-1.0.zip -d <install directory>
cd <install directory>
chmod u+x *.sh
./startKafkaMonitor.sh
```

* Connect with your browser at http://your-host:4567/kafka-monitor/
* Stop application with `stopKafkaMonitor.sh`

## Release Notes

### v1.0 (29/06/2019)
* Initial release
