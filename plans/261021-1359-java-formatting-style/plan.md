# Java Formatting Style

## Goal

Make Spotless-generated Java formatting easier to read without changing application behavior.

## Plan

1. Compare the current AOSP Google Java Format output with the default Google Java Format output.
2. Keep the style change only if it improves nested calls and remains consistent across the Java source tree.
3. Run Spotless, compile, and the backend test suite.

## Scope

- Formatter configuration in `build.gradle`.
- Java source files changed by Spotless only.

## Success Criteria

- `spotlessCheck`, `compileJava`, and tests pass.
- No behavioral code changes.
- The formatting rule is explicit and documented by the Gradle configuration.
