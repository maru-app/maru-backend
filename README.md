# maru-backend ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?logo=kotlin&logoColor=white) 	![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?logo=spring&logoColor=white) [![Check ktlint and Run unit tests](https://github.com/SkyLightQP/maru-backend/actions/workflows/ci.yml/badge.svg?event=pull_request)](https://github.com/SkyLightQP/maru-backend/actions/workflows/ci.yml)
> 일기장 서비스 - 마루: 나만의 일기를 쓰는 공간.

## 환경변수
```dotenv
PORT=8080
# Frontend domain (without schema, trailing slash)
DOMAIN=acme.com
# Backend domain
API_DOMIAN=api.acme.com

DATABASE_URL=jdbc:postgresql://localhost:5432/database
DATABASE_USERNAME=
DATABASE_PASSWORD=

# Redirect URL after initial social login
OAUTH_REDIRECT_URL=http://frontend.com/register
# Redirect URL after successful social login
OAUTH_SUCCESS_URL=http://localhost

ACCESS_TOKEN_SECRET=
ACCESS_TOKEN_EXPIRATION=
REGISTER_TOKEN_SECRET=
REGISTER_TOKEN_EXPIRATION=

GOOGLE_CLIENT_ID=
GOOGLE_SECRET=

NAVER_CLIENT_ID=
NAVER_SECRET=

DIARY_SECRET=

# Separate by comma
CORS_ORIGINS=http://localhost:3000,http://frontend

MINIO_ENDPOINT=
MINIO_ACCESS_KEY=
MINIO_SECRET_KEY=
MINIO_BUCKET_NAME=
MINIO_PRESIGNED_URL_EXPIRATION=

TRUSTED_PROXIES=
```

## 프론트엔드

- [maru-frontend](https://github.com/SkyLightQP/maru-frontend)

## 라이센스

- 해당 프로젝트는 일기장 파일 관리를 위해 [`minio`](https://github.com/minio/minio) 프로젝트를 사용하고 있습니다. 이에 따라 `AGPL` 라이센스를 적용받습니다.
- [GNU Affero General Public License v3.0](./LICENSE)

