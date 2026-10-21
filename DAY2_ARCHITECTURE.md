# Day 2 Architecture: Service Boundaries, Database Schema, API Contracts

## Repository
`Dots_and_Box_game` — Classic dots and boxes game (Java Swing)

## Proposed Concept
GridBattle: Real-time multiplayer gaming platform with WebSocket matchmaking, ELO rankings, tournaments, AI opponents, replay system, and spectator mode.

## Service Boundaries
| Service | Responsibility | Technology |
|---------|---------------|------------|
| Game Engine | Core dots-and-boxes logic, square detection, score calculation | Java (standalone module) |
| Client (JavaFX) | Desktop GUI, board rendering, move input, animations | JavaFX |
| Client (Web/React) | Browser-based gameplay, WebSocket client | React, TypeScript |
| Client (Mobile) | React Native/Flutter wrapper sharing WebSocket protocol | React Native/Flutter |
| Matchmaking Service | Skill-based matching, queue state, ready check | Spring Boot, WebSocket, Redis |
| Game Server | Real-time move synchronization, room state, validation | Spring Boot, WebSockets, PostgreSQL |
| Rating Service | ELO/Glicko-2 calculation, leaderboards, rankings | Spring Boot, PostgreSQL |
| AI Service | Minimax with alpha-beta pruning, difficulty tiers | Python/Java, self-play training |
| Replay Service | Move recording, playback, analysis, storage | Spring Boot, object storage |
| Tournament Service | Brackets, scheduling, Swiss-system, registration | Spring Boot, PostgreSQL |
| Auth Service | Registration, login, JWT, profiles | Spring Security, PostgreSQL |
| Chat Service | Room chat, emoji, moderation, profanity filter | Spring Boot, WebSockets |

## Database Schema (PostgreSQL)
- `users` — id, username, email, password_hash, avatar, display_name, created_at
- `user_profiles` — id, user_id, timezone, settings, stats (wins/losses/draws), updated_at
- `games` — id, room_id, board_size, variant, status (waiting/active/completed), winner_id, created_at, completed_at
- `game_moves` — id, game_id, user_id, dot1, dot2, line_id, square_completed, timestamp
- `rooms` — id, name, player_slots, status (open/full/closed), created_at
- `room_participants` — id, room_id, user_id, joined_at, disconnected_at
- `ratings` — id, user_id, rating (Glicko-2), rating_deviation, volatility, updated_at
- `leaderboards` — id, period (daily/weekly/monthly), user_id, rank, score, created_at
- `replays` — id, game_id, moves_json, metadata, storage_url, created_at
- `tournaments` — id, name, type (single_elimination/round_robin/swiss), status, created_at
- `tournament_matches` — id, tournament_id, player1_id, player2_id, winner_id, round, created_at
- `friends` — id, user_id, friend_id, status, created_at
- `challenges` — id, challenger_id, challenged_id, status, expires_at, created_at
- `notifications` — id, user_id, type, message, read, created_at

## API Contracts (REST + WebSocket)
### REST (Spring Boot)
- `POST /api/v1/auth/register` → {user, token}
- `POST /api/v1/auth/login` → {user, token}
- `GET /api/v1/users/{id}/profile` → {profile, stats}
- `GET /api/v1/games` → list (user's history)
- `GET /api/v1/games/{id}` → {game, moves}
- `GET /api/v1/ratings/leaderboard` → list (paginated)
- `POST /api/v1/rooms` → {room}
- `GET /api/v1/rooms/{id}` → {room, participants}
- `POST /api/v1/challenges` → {challenge}
- `GET /api/v1/tournaments` → list
- `POST /api/v1/tournaments/{id}/register` → 204
- `GET /api/v1/replays/{id}` → {replay, moves}
- `GET /api/v1/notifications` → list

### WebSocket (STOMP over WebSocket)
- `/app/room.create` → create room, broadcast
- `/app/room.join` → join room, broadcast participant update
- `/app/move` → {gameId, dot1, dot2} → validate, broadcast to room
- `/app/rematch` → rematch request, broadcast
- `/app/chat` → {roomId, message} → broadcast to room
- `/app/spectate` → {gameId} → join as observer, receive moves
- `/topic/room/{id}` → room state updates
- `/topic/game/{id}` → move broadcasts
- `/topic/rating` → leaderboard updates

## Technical Notes
- Game engine extracted from Swing UI into standalone module (usable by JavaFX and web)
- WebSocket protocol with JSON message serialization
- Glicko-2 rating system (more accurate than ELO for dynamic matchmaking)
- Minimax with alpha-beta pruning for AI; difficulty tiers (easy/medium/hard)
- Replay storage in S3-compatible object storage
- Multiple board sizes (5x5 to 15x15) and game variants (timed, capture-the-square, handicap)
- No backend yet; Java Swing desktop only

## Day 2 Outcome
Architecture design documented. No code changes. 0 PRs. Ready for Day 3 (CI/CD pipeline).