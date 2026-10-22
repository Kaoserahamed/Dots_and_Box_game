# Day 5 Authentication: Registration, Login, JWT Sessions, Password Hashing

## Repository
`Dots_and_Box_game` — Classic dots and boxes game (Java Swing)

## What was built

The authentication service lives in the server module, in
`server/src/main/java/com/makesquare/server/auth`, split by concern so that each piece is small
enough to read on its own:

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

`GET /api/v1/status` is also public, because it is how a caller finds out whether the server is
up before it has a token.

## The rules

* **A name and an address can each be taken only once.** A second attempt is a conflict, not a
  silent overwrite.
* **A password is never stored, only its bcrypt hash.** bcrypt is used rather than a plain
  digest, because a password is a low-entropy secret that somebody might try to guess: it is
  deliberately slow, and it salts every password, so two accounts with the same password get
  different hashes.
* **A wrong name and a wrong password produce the same answer.** The endpoint cannot be used to
  find out which names have accounts.
* **The token check is deny-by-default.** Any endpoint that is not explicitly public has to
  carry a valid token, or the request is refused with a 401 before it reaches a handler.
* **Signing out revokes the token at once** rather than leaving it to expire. Revoked tokens are
  remembered by their digest, so the store cannot be read to obtain a usable credential.

## The token

A JSON Web Token signed with HMAC-SHA256. It carries the account's identifier, its name and its
role, and it is verified by signature and expiry on every request — so the server knows who the
caller is without a database round trip, and a client cannot invent a token for an account it
does not own.

The signing secret comes from `AUTH_JWT_SECRET` and must be overridden in every deployment that
is not a local development run. It must be at least 32 bytes, so that HMAC-SHA256 is used rather
than a weaker algorithm.

## Storage

Accounts are kept in memory, because the database arrives with its own piece of work (Day 7).
`UserRepository` is the seam that storage changes behind: the PostgreSQL schema and its
migrations replace the in-memory implementation without changing `AuthService` or anything above
it.

## The dependencies

Only Spring Security's *cryptography* is taken, not the whole security framework. The token is
checked by the authentication middleware, and the one thing wanted from Spring Security is a
well tested password hasher. The JSON Web Token library is `jjwt`.

## Tests

Four test modules cover the credentials, the tokens, the rules and the endpoints end to end
through the real filter chain:

| Test module | Covers |
|-------------|--------|
| `PasswordServiceTest` | A hash is not the password, the right password verifies, a wrong one does not, two hashes of one password differ |
| `JwtServiceTest` | A token carries the account, a tampered token is refused, a token signed with another secret is refused, a malformed token is refused |
| `AuthServiceTest` | Registration, the taken name and address, sign in, the identical answer for a wrong name and a wrong password, signing out revoking the token at once |
| `AuthControllerTest` | The endpoints through the real filter chain: registration, a conflict, sign in, a refusal, a protected endpoint with and without a token, and signing out |

The suite went from 31 tests to 65, and the whole build — Maven, Gradle and the image — passes
every gate on GitHub Actions.

## Day 5 Outcome
Authentication module implemented: registration, sign in, JWT-based session management and
password hashing. 1 PR created targeting `development`.
