# bcparis-service

The bcparis-service is standalone restful service which process messages using others different systems.

## Build

Clone [iamp-commons](https://github.com/bcgov-c/iamp-commons)

```
git clone https://github.com/bcgov-c/iamp-commons.git
```

Install common-metrics

```
cd common-metrics
mvn install
```

Install root-pom found in the iamp-commons root directory

```
cd ..
mvn install
```

Install spring-boot-api-service

```
cd spring-boot-api-service
mvn install
```

install [com.splunk.logging:splunk-library-javalogging](https://github.com/splunk/splunk-library-javalogging) v 1.6.2

```
git clone https://github.com/splunk/splunk-library-javalogging
cd splunk-library-javalogging
git checkout tags/1.6.2
mvn install
```

clone this repository

```
git clone https://github.com/bcgov/bcparis-service
cd bcparis-service
mvn install
```

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Run locally

Build the application using the [Build](#Build)

Install [MySql](https://www.mysql.com/), bellow is an example for running mysql on docker

```bash
docker run --name test-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=<set_password> -e MYSQL_DATABASE=metastore -d mysql:latest
```

Create a `config` folder at the root of the solution

```bash
mkdir config
```

Add a new file `logback.xml` in the config

```bash
cd config
touch logback.xml
```

add the following content to the file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true">
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <springProperty scope="context" name="custom-pattern" source="logging.pattern.custom" />
    <property name="CONSOLE_LOG_PATTERN" value="%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) ${custom-pattern} %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"/>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
    <springProperty scope="context" name="splunk-url" source="splunk.hec.url"/>
    <springProperty scope="context" name="splunk-token" source="splunk.hec.token"/>
    <springProperty scope="context" name="application-name" source="spring.application.name"/>
    <appender name="splunk" class="com.splunk.logging.HttpEventCollectorLogbackAppender">
        <url>${splunk-url}</url>
        <token>${splunk-token}</token>
        <source>${application-name}</source>
        <host>${HOSTNAME}</host>
        <disableCertificateValidation>true</disableCertificateValidation>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>%msg</pattern>
        </layout>
    </appender>
    <root level="INFO">
        <appender-ref ref="splunk"/>
    </root>
</configuration>
```

### IntelliJ

Install [LomBok](https://projectlombok.org/) plugin from marketplace

> File > Settings > Plugins > Search in repositories > LomBok

Restart

Run Mvn install

If you are experiencing error in the code run Files > Invalidate Caches/Restart.
also you can do Right click > Maven > Reimport

Set up the environment variables

edit the application configuration and 

Configure all the environment variables in `.env.template` file.

use the [SOAP UI collection](src/test/soapui/bcparis-service-soapui-project.xml) to interact with the service.
Set up the project url to `http://localhost:8080`

### Type of processed messages

- POR - Oracle Protection Order Registry BC41029
- Driver - ICBC BC41027
- Vehicle - ICBC BC41028
- Satellite - Check Performance/Round trip execution time

### Message Sample

```json
{
   "Envelope":{
      "Header":{
         "Origin":{
            "QName":"CPIC.MSG.AGENCY.REMOTE",
            "QMgrName":"BMVQ3VIC"
         },
         "CPICVer":"1.4",
         "UDF":"BCPARISTEST",
         "Priority":"1",
         "Routing":{
            "QName":"CPIC.MSG.BMVQ3VIC",
            "QMgrName":"BMVQ3VIC"
         },
         "MsgSrvc":{
            "msgActn":"Receive"
         }
      },
      "Body":{
         "MsgFFmt":"<![CDATA[SN:M00001-0001 MT:MUF MSID:BRKR-190515-20:02:21 FROM:BC41127 TO:BC41027 TEXT:RE: 8372\nHC IC80300\nBC41027 \nBC41028\nSNME:SMITH/G1:JANE/G2:MARY/DOB:19000101/SEX:F\n\n2019051520022120190515200221\n]]>"
      },
      "MQMD":{
         "feedback":"0",
         "replyToQueueManagerName":"CBQ3",
         "messageIdByte":"41 4D 51 20 43 42 51 33 20 20 20 20 20 20 20 20 D9 BF 03 5D 85 20 6E 23",
         "messageType":"8",
         "format":"MQSTR",
         "replyToQueueName":"CPIC.RPT.MSG",
         "correlationIdByte":"41 4D 51 20 42 4D 56 51 33 56 49 43 20 20 20 20 5D 0A D2 81 20 38 61 02"
      }
   }
}
```

### SWAGGER Endpoint

Available on DEV environment.
https://bcparis-service-xqb2qz-dev.pathfinder.bcgov/swagger-ui.html


### Endpoints

**Message Endpoint**
**PUT /api/v1/message**
Process a message.

**Test Endpoints**
**POST /api/v1/message/test/layer7**
Send a message to Layer 7.

**POST /api/v1/message/test/satellite**
Send a Satellite Message.

**POST /api/v1/message/test/satellite/all**
Send all the 13 Satellite Messages.

### High Level Design and Messages

For more information about the High Level Design  and Messages check the confluence page.
[https://justice.gov.bc.ca/wiki/display/APILM/7.3+High+Level+Design](https://justice.gov.bc.ca/wiki/display/APILM/7.3+High+Level+Design)


## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [release on this repository](https://github.com/bcgov/bcparis-service/releases). 

### Update Version

Run

```
mvn versions:set -DartifactId=*  -DgroupId=*
```

### Copyright

 ```
Copyright 2019 Province of British Columbia

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at 

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
