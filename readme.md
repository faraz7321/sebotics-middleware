# Sebotics Middleware

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

## Building

```bash
./scripts/run.sh          # clean build and start with default profile
./scripts/run.sh --skip-tests --profile dev  # faster cycle using in-memory DB
```
