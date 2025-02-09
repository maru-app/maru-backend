# maru-backend ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?logo=kotlin&logoColor=white) 	![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?logo=spring&logoColor=white)

> 일기장 서비스 - 마루: 나만의 일기를 쓰는 공간.

# 환경변수
```dotenv
PORT=8080

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

CORS_ORIGINS=http://localhost:3000,http://frontend
```

# 프론트엔드

- [maru-frontend](https://github.com/SkyLightQP/maru-frontend)

# 라이센스

- [GNU Affero General Public License v3.0](./LICENSE)

