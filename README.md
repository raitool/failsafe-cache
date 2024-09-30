
```
L1     - Guava InMemory - Expires in 1 HOUR
L2     - Redis cache    - Expires in 1 DAY
SOURCE - REST endpoint
```

| # | L1 - InMemory | L2 - Redis      | Source    | L1 expected   | L2 expected            | Source expected | Expected result |
|---|---------------|-----------------|-----------|---------------|------------------------|-----------------|-----------------|
| 1 | FRESH         | FRESH / EXPIRED | UP / DOWN | `not updated` | `not updated`          | `not queried`   | IN_MEMORY       |
| 2 | EXPIRED       | FRESH           | UP        | `updated`     | `updated`<sup>c1</sup> | `200_OK`        | SOURCE          |
| 3 | EXPIRED       | EXPIRED         | UP        | `updated`     | `updated`              | `200_OK`        | SOURCE          |
| 4 | EXPIRED       | DOWN            | UP        | `updated`     | `not updated`          | `200_OK`        | SOURCE          |
| 5 | EXPIRED       | FRESH           | DOWN      | `not updated` | `not updated`          | `5xx_...`       | REDIS           |
| 6 | EXPIRED       | EXPIRED         | DOWN      | `not updated` | `not updated`          | `5xx_...`       | `5xx_...`       |

<sup>c1</sup> if it is NOT updated, then we don't gain 1 DAY downtime support

