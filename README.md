# maru-backend ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?logo=kotlin&logoColor=white)    ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?logo=spring&logoColor=white)

> 마루 - 나만의 일기를 쓰는 공간

## 시작하기

```shell
docker build -t maru-backend .
docker run -p 8080:8080 --env-file .env -v /var/log/maru:/logs maru-backend
```

또는 Docker Compose를 사용해서 개발 환경을 시작할 수 있어요.

```shell
docker compose up -f ./docker-compose.dev.yml -d
```

## 데이터베이스 ERD

- [ERD](https://www.erdcloud.com/d/xr2BoSnxayTL3QyMt)

## 환경변수

- [.env.example](./.env.example) 파일을 참고해주세요.

```shell
cp .env.example .env
```

## 라이센스

- 마루에서는 첨부파일 저장을 위해 [`MinIO`](https://github.com/minio/minio)를 사용하고 있어요. <br/>이에 따라 `maru-backend`도 `AGPL` 라이센스를 적용받아요.
- [GNU Affero General Public License v3.0](./LICENSE)

