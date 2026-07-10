# Contributing to PotoFlux

Thank you for your interest in contributing! Below are the main ways you can get involved.

## Code Contributions
1. **Fork the repository** on GitHub.
2. **Create a feature branch**.
3. Implement your change, following the existing code style (see `STYLE_GUIDE.md`).
4. **Test the app** (`./gradlew run`) and ensure the app doesn't crash and your features work.
5. Commit your changes with clear, conventional commit messages.
6. Push the branch to your fork and open a **Pull Request** against `main`.

All pull requests are reviewed by the maintainers. Please make sure your PR:
- Describes the problem and solution clearly.
- Includes unit/integration tests if applicable.
- Does not break existing functionality.

## Documentation & Translations
- **Documentation**: Update or add documentation in `docs/branch-name` branches. Consider making clear and useful doc for the people coming after.
- **Translations**: Fork the repo, add new language support in [AbstractTranslationRegistry](src/main/java/net/minheur/potoflux/translations/AbstractTranslationsRegistry.java) (see existing `en()` and `fr()` methods). Then use those methods to add the translations.

## Community Support via Discord
You can also help by answering questions, providing feedback, or discussing ideas on our Discord server:

[![Discord Server Invite](https://invite.casperiv.dev?inviteCode=wCTcVnJFmx)](https://discord.gg/wCTcVnJFmx)

Feel free to ping **@minheur2000** for guidance. While the community is primarily French‑speaking, English is welcome. Please be respectful and patient with members of all language backgrounds.

## Guidelines & Code of Conduct
All contributors must adhere to the project's [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you agree to:
- Treat everyone with respect.
- Use inclusive language.
- Follow the licensing terms (MIT) for any contributions.

## Setting Up Your Development Environment
```bash
# Clone the repo
git clone https://github.com/yourusername/PotoFlux.git
cd PotoFlux
# Build the project (requires Java 17+ and Gradle)
./gradlew build
# Run the application
./gradlew run
```

## Submitting a Pull Request
After pushing your branch, open a PR on GitHub. Fill out the PR template, linking any related issues. The maintainers will review, request changes if needed, and merge once approved.

---

Thank you for helping make PotoFlux better!
