# VibeLibrarian

This is a vibe coding test using [Aider](https://aider.chat) and [Gemini 2.5 Pro Preview](https://gemini.google.com/app).

It managed to pretty much one shot this simple app.

The goal of this app is to look up an ISBN (book international identifier) using OpenLibrary's API.

Targeted platforms: Desktop, web(wasm), Android.

I gave CONVENTIONS.md to aider and prompted it with this:
``` I want to build an app that looks up an ISBN and displays all info from that ISBN. For now, assume the user is typing the ISBN number. I want the same app for desktop, web (wasm if possible, js otherwise) and Android.```

Aider failed to isolate the feature in a dedicated module but that's not too bad.

That wasm hint actually bit me in the ass a bit due to bad wasm support in Ktor (for now).

## Distribution

Quite a few minor struggles on that front but AI still helps and does most of the job.

I absolutely spent more time on this than on the actual app.

The "take a screenshot of an error and paste it to the AI" was quite handy here.

### Web

https://arosov.github.io/vibelibrarian/

The issue for that one was a simple mistake in the gradle task to run to actually have something that
can be distributed, so not bad.

## Windows

First msi package generated installs successfully but JVM fails to start properly at runtime.

Fixed by changing Ktor engine to CIO, not exactly sure I understand the initial issue.

Minor adjustments done manually to have a nicer Windows install that doesn't use the package name
as a user-visible name.

## Android

To be tested

# Default KMP Readme
This is a Kotlin Multiplatform project targeting Android, Web, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.