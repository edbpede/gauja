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
