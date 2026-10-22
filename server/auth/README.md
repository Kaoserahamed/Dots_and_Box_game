# The Authentication Module

The authentication service answers *who is calling*. It lives in
`server/src/main/java/com/makesquare/server/auth` and is split by concern, so
that each piece is small enough to read on its own.

## What it does

| Concern | Class |
|---------|-------|
| The account itself | `UserModel` |
| Where accounts are kept | `UserRepository`, `InMemoryUserRepository` |
| Password hashing | `PasswordService` |
| Session tokens | `JwtService`, `RevokedTokenService` |
| The rules | `AuthService` |
| The HTTP surface | `AuthController` |
| The token check | `AuthMiddleware` |
| The refusals | `AuthException`, `AuthExceptionHandler` |

The request and response contracts are in `dto`.

## Endpoints

| Endpoint | Needs a token | Purpose |
|----------|----------------|---------|
| `POST /api/v1/auth/register` | no | Create an account and sign in |
| `POST /api/v1/auth/login` | no | Sign in |
| `GET /api/v1/auth/me` | yes | The account the caller is signed in as |
| `POST /api/v1/auth/logout` | yes | Sign out, and revoke the token at once |

`GET /api/v1/status` is also public, because it is how a caller finds out
whether the server is up before it has a token.

## The rules

* A name and an email address can each be taken only once.
* A password is never stored, only its bcrypt hash.
* A wrong name and a wrong password produce the same answer, so the endpoint
  cannot be used to find out which names have accounts.
* The token check is deny-by-default: any endpoint that is not explicitly
  public has to carry a valid token, or the request is refused with a 401
  before it reaches a handler.
* Signing out revokes the token at once rather than leaving it to expire.

## The token

A JSON Web Token, signed with HMAC-SHA256. It carries the account's
identifier, its name and its role, and it is verified by signature and expiry
on every request. The signing secret comes from `AUTH_JWT_SECRET` and must be
overridden in every deployment that is not a local development run; it must be
at least 32 bytes so that HMAC-SHA256 is used.

## Storage

Accounts are kept in memory, because the database arrives with its own piece of
work. `UserRepository` is the seam that storage changes behind: the PostgreSQL
schema and its migrations replace the in-memory implementation without
changing `AuthService` or anything above it.

## Day 5 Outcome
Authentication module implemented: registration, sign in, JWT-based session
management and password hashing. 1 PR created targeting `development`.
