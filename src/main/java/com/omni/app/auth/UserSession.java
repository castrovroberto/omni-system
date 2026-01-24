package com.omni.app.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an authenticated user session.
 *
 * <p>Sessions are created when a user logs in and can expire after a configurable duration.
 */
public class UserSession {

  /** Default session duration of 1 hour. */
  public static final Duration DEFAULT_DURATION = Duration.ofHours(1);

  private final String token;
  private final User user;
  private final Instant createdAt;
  private final Instant expiresAt;
  private Instant lastAccessedAt;

  /**
   * Creates a new session for the given user with default duration.
   *
   * @param user the authenticated user
   */
  public UserSession(User user) {
    this(user, DEFAULT_DURATION);
  }

  /**
   * Creates a new session for the given user with specified duration.
   *
   * @param user the authenticated user
   * @param duration how long the session should be valid
   */
  public UserSession(User user, Duration duration) {
    this.user = Objects.requireNonNull(user, "User cannot be null");
    this.token = generateToken();
    this.createdAt = Instant.now();
    this.lastAccessedAt = this.createdAt;
    this.expiresAt = this.createdAt.plus(duration);
  }

  private static String generateToken() {
    return UUID.randomUUID().toString();
  }

  /**
   * Returns the session token.
   *
   * @return the unique session token
   */
  public String getToken() {
    return token;
  }

  /**
   * Returns the user associated with this session.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * Returns when this session was created.
   *
   * @return the creation timestamp
   */
  public Instant getCreatedAt() {
    return createdAt;
  }

  /**
   * Returns when this session expires.
   *
   * @return the expiration timestamp
   */
  public Instant getExpiresAt() {
    return expiresAt;
  }

  /**
   * Returns when this session was last accessed.
   *
   * @return the last access timestamp
   */
  public Instant getLastAccessedAt() {
    return lastAccessedAt;
  }

  /**
   * Checks if this session has expired.
   *
   * @return true if the session has expired
   */
  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  /**
   * Checks if this session is still valid (not expired).
   *
   * @return true if the session is valid
   */
  public boolean isValid() {
    return !isExpired();
  }

  /**
   * Updates the last accessed timestamp to now.
   *
   * @return the new last accessed timestamp
   */
  public Instant touch() {
    this.lastAccessedAt = Instant.now();
    return this.lastAccessedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSession that = (UserSession) o;
    return Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    return String.format(
        "UserSession[user=%s, token=%s..., expired=%s]",
        user.getUsername(), token.substring(0, 8), isExpired());
  }
}
