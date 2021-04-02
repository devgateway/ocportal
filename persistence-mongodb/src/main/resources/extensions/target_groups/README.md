# Target Groups

Procurement Plan, Tender and Organization AGPO Categories

```
{
  "definitions": {
    "TargetGroup": {
      "title": "AGPO Category",
      "description": "The name of the target group. Eg PWD, Women, Youth, etc.",
      "type": [
        "string",
        "null"
      ]
    },
    "Item": {
      "properties": {
        "targetGroup": {
          "title": "AGPO Category",
          "$ref": "#/definitions/TargetGroup"
        },
        "targetGroupValue": {
          "title": "AGPO Category Value",
          "description": "The monetary value of a single unit, allocated to the AGPO Category",
          "$ref": "#/definitions/Value"
        }
      }
    },
    "Tender": {
      "properties": {
        "targetGroup": {
          "title": "AGPO Category",
          "$ref": "#/definitions/TargetGroup"
        }
      }
    },
    "Organization": {
      "properties": {
        "targetGroup": {
          "title": "AGPO Category",
          "$ref": "#/definitions/TargetGroup"
        }
      }
    }
  }
}
```