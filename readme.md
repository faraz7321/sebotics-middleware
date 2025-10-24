# Sebotics Middleware

## Endpoint structure

All HTTP routes are versioned under `/api/v1` with resource-specific folders under `src/main/java/sebotics/middleware/api/v1/`:

- `device` implements
  - `POST /api/v1/device/register`
  - `DELETE /api/v1/device/unregister/{deviceId}`
  - `GET /api/v1/device/info`
- `lift` contains stubs (currently return HTTP 501) for
  - `POST /api/v1/lift/bind`
  - `POST /api/v1/lift/unbind`
  - `POST /api/v1/lift/call`
  - `GET /api/v1/lift/status`
  - `POST /api/v1/lift/reserve`
  - `POST /api/v1/lift/cancel`

Shared response and exception utilities live in `api/common`. Each version folder mirrors the REST path, hosting its own controller, API service, and DTO sub-packages so new endpoints can be scaffolded quickly.

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
