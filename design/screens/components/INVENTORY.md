<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Content-component inventory

All components are native implementations of a shared behavior contract. They accept domain values and events, never generated DTOs, services or mutable global state. Phase 4 implements them; this inventory does not claim native UI exists.

| Component | Contract |
|---|---|
| [TitleCard](TitleCard.md) | Compact movie/TV identity shared by Discover, search and related-media lists. |
| [MediaSlider](MediaSlider.md) | Ordered, titled horizontal media collection on Discover or a detail screen. |
| [RequestCard](RequestCard.md) | Request summary for request lists and recent requests. |
| [RequestBlock](RequestBlock.md) | Inline request history on movie/TV details. |
| [RequestButton](RequestButton.md) | Permission-aware entry point to movie/TV request creation. |
| [IssueBlock](IssueBlock.md) | An issue summary or comment block with ownership and resolution state. |
| [StatusBadge](StatusBadge.md) | Compact availability or request-state label; semantic families are shared across platforms. |
| [AirDateBadge](AirDateBadge.md) | Localized release/air date and upcoming state. |
| [PersonCard](PersonCard.md) | Cast, crew and search-result person identity. |
| [CompanyCard](CompanyCard.md) | Studio or network browse entry. |
| [GenreCard](GenreCard.md) | Visual genre entry into a media-type-specific browse grid. |
| [GenreTag](GenreTag.md) | Small selectable genre label in details and filter editors. |
| [KeywordTag](KeywordTag.md) | Keyword label and filter control. |
| [DownloadBlock](DownloadBlock.md) | Read-only request download progress, without playback. |
| [ExternalLinkBlock](ExternalLinkBlock.md) | Explicit external metadata and trailer link-outs. |
| [BlocklistedTagsBadge](BlocklistedTagsBadge.md) | Show that a title/collection is excluded from discovery. |
| [PermissionEdit](PermissionEdit.md) | Full Seerr permission matrix used by user/default/bulk editors. |
| [PermissionOption](PermissionOption.md) | One labeled permission control inside PermissionEdit. |
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
