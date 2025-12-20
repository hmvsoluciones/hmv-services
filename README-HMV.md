# Docker

```sh
docker compose -f postgresql.yml up -d

docker compose -f src/main/docker/postgresql.yml up -d
```

## Importar jdl

jhipster import-jdl .\jdl.jdl

## Regrenarar plantillas

jhipster jdl.\jdl.jdl --force
