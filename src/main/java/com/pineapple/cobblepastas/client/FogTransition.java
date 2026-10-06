package com.pineapple.cobblepastas.client;

/** A four-second, frame-rate-independent fade that also handles reversing at a border. */
final class FogTransition {
  private static final double TRANSITION_NANOS = 4_000_000_000.0;
  private Object world;
  private long previousTime;
  private float amount;

  float update(Object currentWorld, boolean corrupted, long now) {
    if (world != currentWorld) {
      world = currentWorld;
      previousTime = now;
      amount = corrupted ? 1.0f : 0.0f;
      return amount;
    }
    double elapsed = Math.max(0.0, (double) (now - previousTime));
    previousTime = now;
    float step = (float) (elapsed / TRANSITION_NANOS);
    amount = corrupted ? Math.min(1.0f, amount + step) : Math.max(0.0f, amount - step);
    return amount;
  }

  void clear() {
    world = null;
    previousTime = 0;
    amount = 0.0f;
  }
}
