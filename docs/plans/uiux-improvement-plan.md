# UI/UX Improvement Plan - Retro Nostalgic Theme

## Objective
Enhance the application's UI/UX using Bootstrap 5.3 with a "Retro Nostalgic" theme.

## Theme Concept: Retro Nostalgic
- **Colors**:
  - Primary: #E07A5F (Terra Cotta)
  - Secondary: #3D405B (Independence Blue)
  - Background: #F4F1DE (Eggshell/Cream)
  - Surface: #FFFFFF (White) with subtle border
  - Accent: #81B29A (Green Sheen)
  - Text: #333333 (Dark Gray)
- **Typography**:
  - Headings: 'Playfair Display', serif (via Google Fonts)
  - Body: 'Courier Prime', monospace or 'Inter', sans-serif (for readability)
- **UI Elements**:
  - Simple borders
  - Box shadows (hard, non-blurred for retro feel)
  - Rounded corners (small radius)

## Execution Tasks

- [x] **1. Setup & Infrastructure**
    - [x] Add Bootstrap 5.3 CDN to `fragments/header.html` (or base layout).
    - [x] Add Google Fonts (Playfair Display, Courier Prime).
    - [x] Create `src/main/resources/static/css/style.css` for custom theme overrides.
    - [x] Configure `spring.web.resources.static-locations` if necessary (usually auto-configured).

- [x] **2. Design System Implementation (CSS)**
    - [x] Define CSS Variables for the color palette.
    - [x] Override Bootstrap defaults (buttons, cards, typography).
    - [x] Implement "Retro" utility classes (e.g., `.retro-box`, `.retro-btn`).

- [x] **3. Component Update**
    - [x] **Navbar**: Update `fragments/header.html` with Bootstrap Navbar, apply retro styling.
    - [x] **Footer**: Create or update footer (if exists).
    - [x] **Alerts**: Style success/error messages.

- [x] **4. Page Implementation**
    - [x] **Login Page (`auth/login.html`)**:
        - Center card layout.
        - Retro styling for form inputs and buttons.
    - [x] **Dashboard (`dashboard/index.html`)**:
        - Card layout for statistics/menu.
        - Improved typography and spacing.
    - [x] **Study Logs (`study_logs/index.html`)**:
        - Table styling (striped, bordered).
        - Action buttons styling.
    - [x] **Landing/Index (`index.html`)**:
        - Hero section with retro typography.
        - Call to Action (CTA) buttons.

- [x] **5. Review & Refine**
    - [x] Verify responsiveness (Mobile/Desktop).
    - [x] Check browser compatibility.
