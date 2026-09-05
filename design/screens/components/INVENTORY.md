<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Content-component inventory

Contracts may be sections here or separate files for substantial unique behavior. All components are native implementations of a shared behavior contract. They accept domain values and events, never generated DTOs, services or mutable global state. Implement them with their consuming flows; this inventory does not claim native UI exists.

| Component | Contract |
|---|---|
| [TitleCard](TitleCard.md) | Compact movie/TV identity shared by Discover, search and related-media lists. |
| [MediaSlider](MediaSlider.md) | Ordered, titled horizontal media collection on Discover or a detail screen. |
| [RequestCard](RequestCard.md) | Request summary for request lists and recent requests. |
| [RequestBlock](RequestBlock.md) | Inline request history on movie/TV details. |
| [RequestButton](RequestButton.md) | Permission-aware entry point to movie/TV request creation. |
| [IssueBlock](IssueBlock.md) | An issue summary or comment block with ownership and resolution state. |
| [StatusBadge](StatusBadge.md) | Compact availability or request-state label; semantic families are shared across platforms. |
| [AirDateBadge](INVENTORY.md#airdatebadge) | Localized release/air date and upcoming state. |
| [PersonCard](PersonCard.md) | Cast, crew and search-result person identity. |
| [CompanyCard](CompanyCard.md) | Studio or network browse entry. |
| [GenreCard](INVENTORY.md#genrecard) | Visual genre entry into a media-type-specific browse grid. |
| [GenreTag](INVENTORY.md#genretag) | Small selectable genre label in details and filter editors. |
| [KeywordTag](INVENTORY.md#keywordtag) | Keyword label and filter control. |
| [DownloadBlock](DownloadBlock.md) | Read-only request download progress, without playback. |
| [ExternalLinkBlock](ExternalLinkBlock.md) | Explicit external metadata and trailer link-outs. |
| [BlocklistedTagsBadge](INVENTORY.md#blocklistedtagsbadge) | Show that a title/collection is excluded from discovery. |
| [PermissionEdit](PermissionEdit.md) | Full Seerr permission matrix used by user/default/bulk editors. |
| [PermissionOption](INVENTORY.md#permissionoption) | One labeled permission control inside PermissionEdit. |
| [QuotaSelector](QuotaSelector.md) | Movie/TV quota amount and rolling window editor or read-only usage summary. |
| [NotificationTypeSelector](NotificationTypeSelector.md) | Server notification-type matrix for user preferences or an agent form. |
| [JSONEditor](JSONEditor.md) | Native editor for Seerr’s webhook payload template. |

## Shared behavior baseline

Components accept immutable domain values and emit events. Data and screen state owners handle I/O, pagination, navigation, permissions and persistence. Components do not own services or global state. Write detailed specifications with the consuming feature; record only applicable states and acceptance criteria, plus deviations from this baseline.

- Screens own initial loading, empty, error, offline and permission-denied behavior where applicable. Preserve valid content/drafts during refresh or failure; show actionable errors without retry loops. Cached reads remain available with screen-owned staleness information; mutations require connectivity. Re-evaluate permissions on profile/permission changes and handle server rejection visibly.
- Adapt wrapping, columns and form width to window/size classes. Preserve stable identities and scroll state for lists. Owning screens provide native list/detail navigation; components do not branch on raw screen width.
- Expose identity, value, selection and actions through TalkBack/VoiceOver, including status beyond color. Support largest text without fixed text heights, native 48 dp/44 pt interactive targets, logical focus order and reduced motion. Use generated theme tokens in both themes.
- Image-bearing components request rendered sizes, cancel obsolete loads, retain layout during loading/failure and use neutral missing-art placeholders. This requirement does not apply to text-only chips or controls.
- Verify applicable component behavior on both platforms. Offline rendering and scroll budgets belong to the containing screen’s performance tests (PRD §9, Phase 11), rather than an acceptance paragraph on each component.

## Small component contracts

These components inherit the shared behavior baseline above. Their references are the corresponding `src/components/<Component>/` directories in Seerr at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). They are consumed by Phase 4 and their owning features.

## AirDateBadge

Localized release/air date and upcoming state.

**Content:** An optional date, media/episode context and an injected clock/time zone for relative presentation. Distinguish unknown date from a released item.

**Actions:** No default action; opening details belongs to the parent.

**Endpoints:** No calls; dates from movie/TV/season domain models.

**Permissions:** No additional permission beyond the containing media.

**Acceptance criteria:** Boundary tests cover midnight, time zones, missing/invalid dates and future dates. The label is meaningful without color or animation.

## BlocklistedTagsBadge

Show that a title/collection is excluded from discovery.

**Content:** Blocklist status, tag/reason labels when supplied, title/collection scope and typed identity.

**Actions:** Open permitted blocklist management; add/remove requires an explicit action and refreshes the aggregate after success.

**Endpoints:** /blocklist and /blocklist/collection/{collectionId}; never the sunset /blacklist alias at the v3.4.1 baseline.

**Permissions:** VIEW_BLOCKLIST for blocklist views; MANAGE_BLOCKLIST for mutations, with ADMIN short-circuit.

**Acceptance criteria:** Unsupported versions disable management with explanation. A failed removal leaves the blocklist label intact; offline writes are unavailable.

## GenreCard

Visual genre entry into a media-type-specific browse grid.

**Content:** Genre ID/name, movie or TV scope, and representative backdrop when supplied.

**Actions:** Open the matching genre-filtered grid and preserve its media type.

**Endpoints:** GET /genres/movie; GET /genres/tv; GET /discover/genreslider/movie; GET /discover/genreslider/tv; GET /discover/movies/genre/{genreId}; GET /discover/tv/genre/{genreId}.

**Permissions:** Signed-in viewing.

**Acceptance criteria:** Empty artwork still exposes the genre name. A TV genre never opens a movie grid.

## GenreTag

Small selectable genre label in details and filter editors.

**Content:** Typed genre ID, localized label, media type, selection state where applicable.

**Actions:** Details navigate to the genre grid; editors toggle selection through an event to the state owner.

**Endpoints:** No direct calls; genre lookup/discover operations are owned by Data.

**Permissions:** Signed-in viewing; editor save permission belongs to its screen.

**Acceptance criteria:** Selected state is conveyed through semantics and an icon, not just color. Touch area meets native minimums even when the visual chip is small.

## KeywordTag

Keyword label and filter control.

**Content:** Typed keyword ID, label, selection/removal state and optional lookup progress.

**Actions:** Open keyword results or toggle/remove in an editor; never treat free text as an ID.

**Endpoints:** GET /search/keyword; GET /keyword/{keywordId}; keyword-filtered discover operations.

**Permissions:** Signed-in viewing; write permission comes from the parent editor.

**Acceptance criteria:** Unknown labels retain their ID until resolved. Cancellation cannot apply an old lookup result to a different keyword.

## PermissionOption

One labeled permission control inside PermissionEdit.

**Content:** Exact flag bit/name, localized title/help, enabled/selected/read-only states and any dependent context.

**Actions:** Emit a proposed toggle, never mutate a global mask or call the API.

**Endpoints:** No calls.

**Permissions:** Inherited from PermissionEdit; a disabled option has an inline reason.

**Acceptance criteria:** TalkBack/VoiceOver announces label, checked state and restriction. Toggling one option changes only the intended bit; unknown flags are not assigned a guessed meaning.
