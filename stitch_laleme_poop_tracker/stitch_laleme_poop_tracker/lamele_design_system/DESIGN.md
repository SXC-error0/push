---
name: Lamele (拉了么) Design System
colors:
  surface: '#fff8f1'
  surface-dim: '#dfd9d1'
  surface-bright: '#fff8f1'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f9f3eb'
  surface-container: '#f4ede5'
  surface-container-high: '#eee7df'
  surface-container-highest: '#e8e1da'
  on-surface: '#1e1b17'
  on-surface-variant: '#40493e'
  inverse-surface: '#33302b'
  inverse-on-surface: '#f7f0e8'
  outline: '#707a6d'
  outline-variant: '#bfcaba'
  surface-tint: '#126d27'
  primary: '#126d27'
  on-primary: '#ffffff'
  primary-container: '#66bb6a'
  on-primary-container: '#004814'
  inverse-primary: '#83da85'
  secondary: '#835500'
  on-secondary: '#ffffff'
  secondary-container: '#feb64c'
  on-secondary-container: '#704800'
  tertiary: '#006685'
  on-tertiary: '#ffffff'
  tertiary-container: '#5eb3d8'
  on-tertiary-container: '#004358'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#9ff79f'
  primary-fixed-dim: '#83da85'
  on-primary-fixed: '#002105'
  on-primary-fixed-variant: '#005318'
  secondary-fixed: '#ffddb4'
  secondary-fixed-dim: '#ffb954'
  on-secondary-fixed: '#291800'
  on-secondary-fixed-variant: '#633f00'
  tertiary-fixed: '#bee9ff'
  tertiary-fixed-dim: '#7ed1f7'
  on-tertiary-fixed: '#001f2a'
  on-tertiary-fixed-variant: '#004d65'
  background: '#fff8f1'
  on-background: '#1e1b17'
  surface-variant: '#e8e1da'
typography:
  display-lg:
    fontFamily: Be Vietnam Pro
    fontSize: 32px
    fontWeight: '800'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  display-mobile:
    fontFamily: Be Vietnam Pro
    fontSize: 24px
    fontWeight: '800'
    lineHeight: '1.2'
  headline-md:
    fontFamily: Be Vietnam Pro
    fontSize: 20px
    fontWeight: '700'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Be Vietnam Pro
    fontSize: 16px
    fontWeight: '500'
    lineHeight: '1.6'
  body-sm:
    fontFamily: Be Vietnam Pro
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.5'
  label-caps:
    fontFamily: Be Vietnam Pro
    fontSize: 12px
    fontWeight: '700'
    lineHeight: '1'
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  margin-mobile: 20px
  gutter: 12px
---

## Brand & Style

The design system is built on a **Cartoon Sticker Aesthetic**, blending digital convenience with the tactile joy of a physical scrapbook. It targets Chinese Gen-Z and young professionals seeking a playful escape from "996" work culture through "shitting-time" humor and stress relief.

The personality is irreverent, encouraging, and "mou" (cute/soft). By utilizing high-contrast outlines and a sticker-like layered depth, the UI avoids any clinical or realistic medical associations. Instead, it treats every interaction as a moment of achievement or a playful break. The emotional response should be one of relief and amusement, turning a mundane biological necessity into a gamified, social, and stress-relieving ritual.

## Colors

The palette is anchored by a warm, paper-like cream background to reduce eye strain and provide a "notebook" feel. 
- **Primary Mint (#66BB6A):** Used for "success" states and primary action buttons, symbolizing freshness and relief.
- **Apricot Accent (#FFB74D):** Used for energy, streak highlights, and playful alerts.
- **Sky Blue (#81D4FA):** Used for social features and informative "chill" moments.
- **Dark Brown Typography (#4E342E):** Replaces harsh blacks with a warm, organic tone that pairs perfectly with the cream base.

Avoid any dark modes or stark whites; the "Off-White/Cream" aesthetic is central to the sticker-book identity.

## Typography

This design system utilizes **Be Vietnam Pro** for its friendly, contemporary curves that complement the rounded UI. For Chinese characters, use a system-default sans-serif (like PingFang SC) with **Bold** weight for headings to maintain the "sticker" impact.

- **Headlines:** Must be chunky and prominent. Use tight letter spacing for large headers to give them a "stamp" look.
- **Body Text:** Maintained at 16px for high legibility during "transit" moments.
- **Hierarchy:** Use font weight rather than just size to differentiate information. Secondary text is rendered in the softer brown (#8D6E63) to reduce visual noise.

## Layout & Spacing

The layout follows a **Fluid Grid** model with generous margins to mimic a loose, organized-yet-playful scrapbook. 

- **Containers:** Content is housed in cards with 20px side margins on mobile.
- **Vertical Rhythm:** Elements are grouped using 16px gaps, while major sections are separated by 32px to allow the cream background to act as "breathing room."
- **Alignment:** While the grid is structured, occasional "tilted" stickers (2-3 degree rotation) can be used for decorative emoji elements to break the rigidity of the digital screen.

## Elevation & Depth

This design system rejects traditional shadows in favor of **Tonal Layers** and **Sticker Outlines**.

1.  **The "Sticker" Look:** Every interactive card and chip features a 2px solid border (using a slightly darker shade of the background or a 10% opacity version of the text color) to make it look like a physical cutout.
2.  **Shadows:** When shadows are used for primary buttons, they are "Hard Shadows"—solid, non-blurred offsets (e.g., 4px down/right) that match the aesthetic of a raised sticker.
3.  **Depth Hierarchy:** 
    *   **Level 0 (Background):** Cream #FFF8F0.
    *   **Level 1 (Cards):** Warm White #FFFBF5 with a subtle border.
    *   **Level 2 (Interaction):** Elements "pop" slightly more using the Hard Shadow when pressed or active.

## Shapes

The shape language is bubbly and exaggerated. 
- **Cards:** 16px (rounded-lg) creates a soft, approachable container.
- **Buttons:** 24px (rounded-xl) or full-pill shapes are used to make them look "squishy" and inviting to tap.
- **Chips/Badges:** 20px (pill) for status indicators and emoji labels.
- **Interactive States:** Use a slight "shrink" scale effect (0.95) on tap to provide tactile feedback without needing complex animations.

## Components

### Buttons
Primary buttons use the Mint Green (#66BB6A) with white or dark brown text. They must have a pill-shape and a 4px hard-shadow offset. Secondary buttons use the Apricot or Sky Blue.

### Cards
Cards are Warm White (#FFFBF5) with a 16px radius. They should never have blurs; use solid borders or subtle tonal shifts to define boundaries.

### Emoji Icons
Crucial: Do not use bathroom iconography. Use high-quality, 3D-style or flat-color emojis. 
- 🌿 for "freshness/completion"
- ⚡ for "speed/efficiency"
- ☁️ for "comfort"
- 🏆 for "achievements"

### Chips & Tags
Used for mood tracking (e.g., "Smooth", "Struggle", "Quick"). These are pill-shaped with background colors matching the Mint, Apricot, or Blue palette at 20% opacity.

### Input Fields
Rounded (16px) with the cream background and a 2px border that turns Mint Green on focus. Use playful placeholder text in Simplified Chinese (e.g., "今天感觉如何？" - How are you feeling today?).

### Progress Bars
Thick, rounded bars. Use the Mint Green for the fill and a light version of the same color for the track. Add a small emoji "runner" at the end of the progress bar for character.