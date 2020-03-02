#Milestone Delayed

M&E reports for Makueni need a delayed option for reporting it as Milestone. This delayed milestone status is not available and the status codelist is closed. We have added a boolean property to the milestone, `delayed` that can be set to `true`

Reports also contain an `authorizePayment` field, which shows if the report recommends proceeding with the actual payment. This is too a boolean property

```{
     "definitions": {
       "Milestone": {
         "properties": {
           "delayed": {
             "title": "Milestone Delayed",
             "type": [
               "boolean",
               "null"
             ]
           },
           "authorizePayment": {
             "title": "Payment Authorization",
             "type": [
               "boolean",
               "null"
             ]
           }
         }
       }
     }
   }
```