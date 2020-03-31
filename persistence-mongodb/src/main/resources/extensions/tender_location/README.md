# Tender Location

Extension of tender entity, that adds location information directly at tender level.
As a dependency we use the ocds_location_extension.

```
{
  "definitions": {
    "Location": {
      "properties": {
        "type": {
          "title": "Type",
          "description": "Location type subdivision",
          "type": [
            "string"
          ],
          "enum": [
            "ward",
            "subcounty"
          ],
          "codelist": "locationType.csv",
          "openCodelist": true
        }
      }
    },
    "Tender": {
      "properties": {
        "locations": {
          "title": "Locations",
          "description": "Location information directly at tender level",
          "type": "array",
          "items": {
            "$ref": "#/definitions/Location"
          }
        }
      }
    }
  }
}
```