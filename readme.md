# Sebotics Middleware

## Project layout & endpoints

### Package map

```
src/main/java/sebotics/middleware/api/v1
├── common/                     # Shared API infrastructure (responses, errors)
├── device/
│   ├── DeviceController.java
│   ├── service/DeviceApiService.java
│   ├── dto/
│   ├── domain/{entity,exception,repository}
│   └── application/service/{Register,Query,Unregister}
└── lift/
    ├── dto/                    # Generic router DTOs (include deviceId)
    ├── router/                 # Public lift endpoints
    │   ├── LiftController.java
    │   └── service/LiftRoutingService.java
    ├── vendor/ElevatorVendorClient.java  # SPI for elevator vendors
    ├── kone/service/KoneLiftService.java
    ├── schindler/service/SchindlerLiftService.java
    └── lutz/service/LutzLiftService.java
```

Device registration stores which vendor a robot prefers; the router uses that to pick the corresponding vendor client.

### REST reference

#### Device lifecycle

| Method | Path                               | Description                     |
|--------|------------------------------------|---------------------------------|
| POST   | `/api/v1/device/register`          | Register device with vendor     |
| DELETE | `/api/v1/device/unregister/{id}`   | Remove device registration      |
| GET    | `/api/v1/device/info`              | Lookup via `serialNumber` or `macAddress` |

Example registration:
```bash
curl -X POST http://localhost:8080/api/v1/device/register \
  -H "Content-Type: application/json" \
  -d '{"serialNumber":"Robot-001","macAddress":"AA:BB:CC:11:22:33","elevatorVendor":"KONE"}'
```

#### Lift router (unified entry point)

Currently returns `501 Not Implemented` until vendor adapters connected:

| Method | Path                     | Notes                         |
|--------|--------------------------|-------------------------------|
| POST   | `/api/v1/lift/bind`      | Body `{ "deviceId": "..." }` |
| POST   | `/api/v1/lift/unbind`    | Body `{ "deviceId": "..." }` |
| POST   | `/api/v1/lift/call`      | Body `{ "deviceId": "..." }` |
| GET    | `/api/v1/lift/status`    | Query `?deviceId=...`          |
| POST   | `/api/v1/lift/reserve`   | Body `{ "deviceId": "..." }` |
| POST   | `/api/v1/lift/cancel`    | Body `{ "deviceId": "..." }` |

Behind the scenes `LiftRoutingService` locates the registered device and forwards to the correct vendor client (`KoneLiftService`, `SchindlerLiftService`, `LutzLiftService`). These services implement `ElevatorVendorClient` and currently return `501` placeholders until real elevator APIs are integrated.

## Running locally

The default configuration targets PostgreSQL (for example a Cloud SQL instance). Create a copy of `.env.example` and fill in your credentials:

```bash
cp .env.example .env
```

The app reads these values automatically when you export them or use a shell that sources `.env`. For quick local development without PostgreSQL you can use the in-memory profile:

```bash
./scripts/run.sh --profile dev
```

This profile uses H2 and automatically recreates schema changes on each run.

## API response format

All endpoints wrap payloads in a consistent envelope:

```json
{
  "errcode": 200,
  "errmsg": "Human readable status",
  "data": { "... response-specific ..." }
}
```

`errcode` aligns with HTTP semantics (e.g., 201 for created resources, 404 for not found, 0 for generic success). `errmsg` carries the human-readable description. The `data` property is omitted when there is no payload, but when validation fails you will receive a list describing the offending fields.

## Building

```bash
./scripts/run.sh          # clean build and start with default profile
./scripts/run.sh --skip-tests --profile dev  # faster cycle using in-memory DB
```
