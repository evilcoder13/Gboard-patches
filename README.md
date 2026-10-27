<h1 align="center">Gboard Patches</h1>

<p align="center">
  Morphe patches for Gboard focused on core productivity, English &amp; Vietnamese input, and BlackBerry-style flick completion.
</p>

<p align="center">
  <a href="https://github.com/jasonwu1994/Gboard-patches/releases/latest"><img alt="Latest release" src="https://img.shields.io/github/v/release/jasonwu1994/Gboard-patches?display_name=tag&label=Release&style=for-the-badge"></a>
  <a href="https://github.com/jasonwu1994/Gboard-patches"><img alt="Total downloads" src="https://img.shields.io/github/downloads/jasonwu1994/Gboard-patches/total?label=Downloads&style=for-the-badge"></a>
  <a href="https://morphe.software/add-source?github=jasonwu1994/Gboard-patches"><img alt="Add to Morphe" src="https://img.shields.io/badge/Morphe-Add%20Source-00A8FF?style=for-the-badge"></a>
  <a href="https://github.com/jasonwu1994/Gboard-patches"><img alt="GitHub stars" src="https://img.shields.io/github/stars/jasonwu1994/Gboard-patches?style=social"></a>
</p>

<p align="center">
  <a href="https://ko-fi.com/jasonwu1994"><img height="40" alt="Buy me a coffee on Ko-fi" src="https://storage.ko-fi.com/cdn/kofi5.png?v=3"></a>
</p>

## Overview

Gboard Patches is a streamlined Morphe source for Gboard enhancements, retaining core usability improvements for English and Vietnamese typing alongside BlackBerry-style flick-up word completion.

## Included Patches

### Project-Built Features

Features designed and built by this project rather than simply unlocking an existing Gboard flag.

<details>
  <summary><code>Flick-Up Word Completion</code></summary>

  Swipe up on the left, center, or right region of the keyboard body to select the corresponding candidate word from the suggestion bar, similar to the BlackBerry 10 / KEYone keyboard.
</details>

<details>
  <summary><code>Clipboard Enhancements</code></summary>

  Lets you enhance clipboard retention time, item count limits, preview lines, countdown and creation-time labels, order index, grid columns, and optionally render only the first 1,000 characters on each clipboard card.
</details>

<details>
  <summary><code>Long-Press Editing Shortcuts</code></summary>

  Add Select all, Undo, Copy, Cut, Paste, and Redo long-press shortcuts to Latin alphabet layouts (English and Vietnamese), with an optional globe-key drag gesture that follows the same physical key positions.
</details>

<details>
  <summary><code>Toolbar Editing Buttons</code></summary>

  Add Select All, Copy, Cut and Paste to the menu. Drag them to the top toolbar and
  use them with different keyboard languages in supported editors.
</details>

<details>
  <summary><code>Incognito Mode Toggle</code></summary>

  Add an Incognito toggle to the Access Point toolbar and configure clipboard and voice typing availability while Incognito mode is active.
</details>

<details>
  <summary><code>Swipe Down to Dismiss Keyboard</code></summary>

  Swipe down from the suggestions or toolbar to close the keyboard.
</details>

<details>
  <summary><code>Latin Globe Key Ignore Interval</code></summary>

  Add an independent English/Latin globe key ignore interval override for post-typing language-switch delay.
</details>

<details>
  <summary><code>Backup &amp; Restore</code></summary>

  Exports all Gboard Patches settings to a portable JSON backup and restores only the modules you select, with per-module and per-key results. It also exports, compares, and restores Gboard's raw PB/XML flag-store files.
</details>


### Gboard Feature Unlocks

Features already present in Gboard that are exposed by enabling hidden settings, rollout gates, or built-in behavior.

<details>
  <summary><code>Advanced Voice Typing</code></summary>

  Enable Advanced Voice Typing with automatic punctuation and speech recognition engine selection.
</details>

<details>
  <summary><code>Enable OCR / Scan Text</code></summary>

  Enable the OCR / Scan Text feature with Latin script recognition (supporting English and Vietnamese).
</details>

<details>
  <summary><code>English QWERTY Up-Flick Uppercase</code></summary>

  Flick up on the English QWERTY keyboard to toggle uppercase and lowercase.
</details>

<details>
  <summary><code>Enable Inline Autofill Suggestions</code></summary>

  Enables inline autofill suggestions in supported contexts.
</details>

<details>
  <summary><code>Grammar Checker</code></summary>

  Enables the <code>Text correction &gt; Grammar check</code> setting and its related rollout gate.
</details>

<details>
  <summary><code>Inline Suggestions</code></summary>

  Enables the <code>Text correction &gt; Smart Compose</code> setting and its related rollout gate.
</details>

<details>
  <summary><code>Use Bluetooth Microphone</code></summary>

  Enables the <code>Voice typing &gt; Use Bluetooth microphone</code> setting and its related rollout gate.
</details>

<details>
  <summary><code>Enable cursor trackpad mode</code></summary>

  Enables the long-press-spacebar trackpad, cursor lock mode, and the required scrub-move preference.
</details>

<details>
  <summary><code>Enable split keyboard</code></summary>

  Enables Gboard's split keyboard layout.
</details>

<details>
  <summary><code>Close Proactive Suggestions</code></summary>

  Shows a dismiss button in the proactive suggestions bar.
</details>

<details>
  <summary><code>Clipboard Custom Character Limit</code></summary>

  Lets you set the maximum number of characters stored for each text clipboard item, with Gboard's stock 20,000-character limit as the default.
</details>

<details>
  <summary><code>Package Rename</code></summary>

  Renames the patched package so it can be installed alongside the official Gboard app.
</details>

## Install

Add this repository as a Morphe source:

- [Open in Morphe](https://morphe.software/add-source?github=jasonwu1994/Gboard-patches)
- Or manually add `https://github.com/jasonwu1994/Gboard-patches`

## Build

Before running Gradle locally, authenticate to Morphe's GitHub Packages registry with either:

- `gpr.user` and `gpr.key` in `~/.gradle/gradle.properties`
- `GITHUB_ACTOR` and `GITHUB_TOKEN` as environment variables

Build the Android patch bundle:

```powershell
.\gradlew.bat :patches:buildAndroid
```

Regenerate patch metadata:

```powershell
.\gradlew.bat generatePatchesList
```

Generated outputs:

- `patches/build/libs/*.mpp`
- `patches-list.json`
- `patches-bundle.json`

## License

Released under the [GNU General Public License v3.0](LICENSE).
