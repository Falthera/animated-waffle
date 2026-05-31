# Architecture

PVP Optimizer uses a small event-driven client architecture:

- `PvpOptimizerClient` registers the client tick hook and owns the controller.
- `JumpResetController` coordinates damage detection, scheduling, cooldowns, and jump execution.
- `DamageValidator` filters invalid damage sources and false positives.
- `CooldownManager` prevents duplicate activations and combat spam retriggers.
- `JumpTimingEngine` schedules the jump on the earliest safe tick.
- `JumpExecutor` performs the actual jump without holding the jump key.
- `PvpOptimizerMixin` bridges the local player damage event into the controller.

The implementation intentionally avoids threads, polling loops, allocations in the hot path, and any user-facing configuration surface.
