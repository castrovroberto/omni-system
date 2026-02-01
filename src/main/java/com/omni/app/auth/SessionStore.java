package com.omni.app.auth;

import com.omni.core.map.MyHashMap;
import com.omni.core.map.MyMap;
import java.util.Optional;

/**
 * Stores and manages user sessions.
 *
 * <p>This class uses {@link MyHashMap} to provide O(1) session lookup by token.
 *
 * <p>Features:
 *
 * <ul>
 *   <li>Create sessions for authenticated users
 *   <li>Lookup sessions by token
 *   <li>Invalidate sessions
 *   <li>Automatic expiration checking
 * </ul>
 */
public class SessionStore {

  private final MyMap<String, UserSession> sessions;

  /** Creates a new empty session store. */
  public SessionStore() {
    this.sessions = new MyHashMap<>();
  }

  /**
   * Creates a new session for the given user.
   *
   * @param user the authenticated user
   * @return the new session
   */
  public UserSession createSession(User user) {
    UserSession session = new UserSession(user);
    sessions.put(session.getToken(), session);
    user.recordLogin();
    return session;
  }

  /**
   * Retrieves a session by its token.
   *
   * <p>If the session exists but is expired, it is automatically invalidated and empty is returned.
   *
   * @param token the session token
   * @return the session if found and valid, empty otherwise
   */
  public Optional<UserSession> getSession(String token) {
    Optional<UserSession> session = sessions.get(token);

    if (session.isPresent() && session.get().isExpired()) {
      invalidateSession(token);
      return Optional.empty();
    }

    // Touch the session to update last accessed time
    session.ifPresent(UserSession::touch);
    return session;
  }

  /**
   * Invalidates a session by token.
   *
   * @param token the session token
   * @return true if the session was found and removed
   */
  public boolean invalidateSession(String token) {
    return sessions.remove(token).isPresent();
  }

  /**
   * Invalidates all sessions for a specific user.
   *
   * @param username the username
   * @return the number of sessions invalidated
   */
  public int invalidateUserSessions(String username) {
    int count = 0;
    // Collect tokens to remove (can't modify while iterating)
    var tokensToRemove = new java.util.ArrayList<String>();

    for (MyMap.Entry<String, UserSession> entry : sessions.entries()) {
      if (entry.getValue().getUser().getUsername().equals(username)) {
        tokensToRemove.add(entry.getKey());
      }
    }

    for (String token : tokensToRemove) {
      if (sessions.remove(token).isPresent()) {
        count++;
      }
    }

    return count;
  }

  /**
   * Returns the number of active sessions.
   *
   * @return the session count
   */
  public int getSessionCount() {
    return sessions.size();
  }

  /**
   * Checks if a token has a valid session.
   *
   * @param token the session token
   * @return true if the token has a valid, non-expired session
   */
  public boolean isValidSession(String token) {
    return getSession(token).isPresent();
  }

  /**
   * Removes all expired sessions from the store.
   *
   * @return the number of expired sessions removed
   */
  public int cleanupExpiredSessions() {
    int count = 0;
    var tokensToRemove = new java.util.ArrayList<String>();

    for (MyMap.Entry<String, UserSession> entry : sessions.entries()) {
      if (entry.getValue().isExpired()) {
        tokensToRemove.add(entry.getKey());
      }
    }

    for (String token : tokensToRemove) {
      if (sessions.remove(token).isPresent()) {
        count++;
      }
    }

    return count;
  }

  /** Clears all sessions from the store. */
  public void clear() {
    sessions.clear();
  }
}
