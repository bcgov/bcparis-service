bcparis-service
==================

The bcparis-service is standalone restfull service which process messages using others different systems.


### Type of processed messages
- POR - Oracle Protection Order Registry BC41029
- Driver - ICBC BC41027
- Vehicle - ICBC BC41028
- Satellite - Check Performance/Round trip execution time

### Message Sample
```
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
