# PotoFlux

PotoFlux is a lightweight **JavaFX** sandbox application released under the **MIT License** ([LICENSE](https://github.com/TechnoMastery/PotoFlux/blob/master/LICENSE)).
The core provides a minimal framework that can be extended through **mods**. Mods act as launchers for other applications, tools for reviewing, productivity helpers, and more. By adding or removing mods you can customise the environment to suit any workflow.

## Architecture

The architecture is intentionally simple:

- **Core** – a thin JavaFX base that handles the window, menu bar, and a tab‑based UI.
- **Mod API** – a well‑documented interface that lets developers register a new tab, add commands to the built‑in terminal, or expose custom settings.
- **Easy-to-use** – each mod is discovered at startup from the `mods/` directory in the user's personal folder and loaded dynamically.

Because the design is straightforward, new contributors can understand the whole system in a few minutes and immediately start building powerful extensions.

## Features

- **Mod‑driven extensibility** – install, enable or disable mods without rebuilding the app.
- **Built‑in terminal** – interact with mods through simple commands.
- **Dynamic UI** – each mod can provide its own tab, toolbar entries, or settings page.
- **Cross‑platform** – runs on any platform that supports JavaFX.

## Getting Started

1. Get your installer on [our website](https://technomastery.github.io/pages/PotoFlux/#dl)
2. Run the installer on your computer
3. Launch the app through the `Potoflux` icon created on your desktop
4. Go to the terminal and tap `modDir`, or in the menu bar: `File > Open mod dir`
5. Drop a mod JAR into the `mods/` folder opened and restart the app.

## Making a Mod

There are no real tutorials on how to make a mod yet.<br>
You can still refer to [the template mod repository](https://github.com/TechnoMastery/PotoFlux-mod-template)
and read to Potoflux's docs, available on [the website](https://technomastery.github.io/pages/PotoFlux/#dl). Please be sure to look for the docs corresponding to your version.
There can be online docs, or you could need to download the jar file and extract it yourself.

## Troubleshooting

If the application crashes, inspect the log files in `logs/`. The exit codes are:

| Exit code | Meaning                         | Fix                                                                        |
|-----------|---------------------------------|----------------------------------------------------------------------------|
| 0         | Normal shutdown                 |                                                                            |
| 1         | Uncaught error in main thread   | Could be anyting. Try relaunching, maybe it appends on a specific action ? |
| 2         | Error during Bootstrap          | Try removing mods, clear your user preferences                             |
| -1        | Event post error                | Try removing mods, check compatibility and duped IDs                       |

When the app crashes, always look for a stack trace. It will help you identify the error !<br>
You can also contact the devs of Potoflux or the mod causing problem.

## Community & Support

Join our Discord community for help, discussions, and to share your mods:

[![Discord Server Invite](https://invite.casperiv.dev?inviteCode=wCTcVnJFmx)](https://discord.gg/wCTcVnJFmx)

## License
Potoflux runs under the [MIT License](https://github.com/TechnoMastery/PotoFlux/blob/master/LICENSE), meaning you can use, modify and distribute it freely.<br>
Please remember to add a copyright notice going towards this GitHub repository.

## Credits

- **Minheur2000** – original creator of the core application.
- **Mkpaz** – theme contributions trough [AtlantaFX](https://github.com/mkpaz/atlantafx) (v2.1.0).