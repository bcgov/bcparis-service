[![Maintainability](https://api.codeclimate.com/v1/badges/583acc1aa9817b970872/maintainability)](https://codeclimate.com/github/bcgov/bcparis-service/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/583acc1aa9817b970872/test_coverage)](https://codeclimate.com/github/bcgov/bcparis-service/test_coverage)

# bcparis-service

BcParis Api is a restfull api that accept Driver, Vehicle and POR(Protection Order Registry) query.

## Messages

| Type | Code | Description |
| --- | --- | --- |
| POR | BC41029 | Protection Order Registry query |
| Driver | BC41027 | Driver query |
| Vehicle | BC41028 | Protection query |

## Get Started

Clone this repository and install dependencies:

```bash
git clone https://github.com/bcgov/bcparis-service
cd bcparis-service
mvn install
```

Set the following [environment variables](#environment-variables) or edit this file `src\main\resources\application.properties`

Run:

```bash
mvn spring-boot:run
```

### Environment Variables

| Environment Variable | Type | Description | Notes |
| --- | --- | --- | --- |
| POR_REST_ENDPOINT | String | String | POR Rest Endpoint | |
| ENDPOINT_POR_REST_USERNAME | String | POR ORDS UserName | |
| ENDPOINT_POR_REST_PASSWORD | String | POR ORDS Password | |
| ENDPOINT_LAYER7_REST_HEADER_USERNAME | String | Layer7 Mq Password | |
| ENDPOINT_LAYER7_REST_HEADER_PASSWORD | String | Layer7 Mq Password | |
| LAYER7_REST_ENDPOINT | String | Layer 7 Mq Password | |
| ENDPOINT_ICBC_REST_HEADER_IMSUSERID | String | ICBC headers userId | |
| ENDPOINT_ICBC_REST_HEADER_IMSCREDENTIAL | String | ICBC headers userId | |
| ENDPOINT_ICBC_REST_HEADER_USERNAME | String | ICBC headers username | |
| ENDPOINT_ICBC_REST_HEADER_PASSWORD | String | ICBC headers password | |
| ENDPOINT_IAMP_EMAIL_SERVICE_REST_USERNAME | String | Email Service username | |
| ENDPOINT_IAMP_EMAIL_SERVICE_REST_PASSWORD | String | Email Service username | |
| EMAIL_SERVICE_RECEIVER | String | Email Service username | |
| EMAIL_SERVICE_ENDPOINT | String | Email service endpont | |
| ENDPOINT_IAMP_EMAIL_SERVICE_REST_PASSWORD | String | Email Service username | |
| METASTOREURL | String | mysql host | |
| SPRING_DATASOURCE_USERNAME | String | mysql username | |
| SPRING_DATASOURCE_PASSWORD | String | mysql password | |
| ICBC_REST_ENDPOINT | String | | |

### Endpoints

#### Message Endpoint

**PUT** /api/v1/message - Process a message.

##### Payload Sample

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

#### Test Endpoints

**POST** /api/v1/message/test/layer7 - Send a message to Layer 7.

#### Swagger UI

[https://localhost:8080/swagger-ui.html](https://localhost:8080/swagger-ui.html)

### IntelliJ

Install [LomBok](https://projectlombok.org/) plugin from marketplace

> File > Settings > Plugins > Search in repositories > LomBok

Restart and run `mvn install`

If you are experiencing errors in the code, run Files > Invalidate Caches/Restart. Also, you can do Right click > Maven > Reimport.

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

## Copyright

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
