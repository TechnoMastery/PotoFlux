# PotoFlux Java Style Guide

## Overview
This style guide defines the conventions used throughout the **PotoFlux** codebase. It aims to keep the code **readable**, **consistent**, and **maintainable** for contributors of any experience level. The guide is enforced by IDE lint settings and the CI pipeline.

## Formatting
- **Indentation**: 4 spaces per level (tab equivalent).
- **Line length**: Avoid more than 120 characters. Break long statements using the standard Java line‑break style.
- **Trailing whitespace**: Never commit trailing spaces.
- **Newlines**: Use Unix `\n` line endings. End each file with a single newline.

## Naming Conventions
- **Classes & Interfaces**: `PascalCase` (e.g., `ModLoader`, `AppController`).
- **Methods & Variables**: `camelCase` (e.g., `loadMods()`, `modDirectory`).
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `DEFAULT_PORT`).
- **Packages**: all lower‑case, reverse‑domain style (`org.potoflux.core`).
- **Test classes**: `<ClassName>Test` (e.g., `ModLoaderTest`).

## Imports
- Use **single‑type imports** rather than wildcards.
- Order imports alphabetically, grouped as:
    1. Any random imports (alphabetical)
    2. `javax.*`
    3. `java.*`
- Separate each group with an empty line.
- You can use IntelliJ IDEA's automatic import system ; currently used.

## Javadoc
- Every **public** class, interface, and method should have a Javadoc comment.
- Include `@param`, `@return`, and `@throws` tags where applicable.
- Use complete sentences, start with a capital letter, and end with a period.

## Logging
- Use the project's [PtfLogger](src/main/java/net/minheur/potoflux/logger/PtfLogger.java) wrapper.
- Log messages at appropriate levels: `.info(...)` for regular flow, `.warning(...)` for recoverable issues, `.error(...)` for failures.

## Testing

- ~~Write **unit tests** for all new public methods using JUnit 5.~~
- ~~Place tests in `src/test/java` mirroring the package structure.~~
- ~~Aim for at least **80% line coverage**. CI will fail if coverage drops below this threshold.~~
- Testing is **not** implemented yet. For now, try doing some manual testing. 

## Git & Pull Requests
- **Branch naming**: `feature/<short-description>`, `bugfix/<short-description>`, `docs/<short-description>`, `lang/<short-description>`.
- **Commit messages**: Use the Conventional Commits format (`type(scope): description`). Example: `feat(mod): add launcher API`.
- **Pull request template**: Fill out the provided template, link related issues, and ensure the PR passes all CI checks before merging.

## Code Reviews
- Reviewers should verify adherence to this style guide, test coverage, and clear documentation.
- Encourage constructive feedback and ask for clarification when needed.

---

_By following this guide, contributors help keep PotoFlux clean, understandable, and easy to extend._
