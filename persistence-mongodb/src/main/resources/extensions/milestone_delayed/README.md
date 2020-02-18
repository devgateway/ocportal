#Milestone Delayed

M&E reports for Makueni need a delayed option for reporting it as Milestone. This delayed milestone status is not available and the status codelist is closed. We have added a boolean property to the milestone, `delayed` that can be set to `true`

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
           }
         }
       }
     }
   }
```