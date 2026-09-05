<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0003: AGPL-3.0-or-later with the App Store Distribution Exception; DCO sign-off, no CLA

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §15](../gauja-prd.md#15-licensing-and-distribution) |

## Context

A copyleft license keeps every fork's source available and covers a future server-side component (the notification relay in ADR 0002) automatically. Apple's and Google's store terms impose restrictions on recipients that the (A)GPL alone does not permit, so distributing a copyleft app there requires an additional permission from every copyright holder. A CLA would collect that permission but adds friction and asymmetry; the Developer Certificate of Origin plus a license statement that includes the exception achieves the same grant.

## Decision

Gauja is licensed AGPL-3.0-or-later. Every copyright holder grants the additional permission under AGPL §7 in `APPSTORE_EXCEPTION.md` (PRD Appendix A) for distribution through Apple's, Google's and comparable app stores. The root `LICENSE` states that "the license" means the AGPL together with that permission, so a DCO sign-off (`git commit -s`, DCO 1.1) grants both. There is no CLA; contributors keep their copyright. Every file carries SPDX headers and the repository is REUSE-compliant; the vendored Seerr spec stays MIT.

## Consequences

- Every commit must be signed off; the `dco-signoff` hook and the GitHub DCO check enforce it.
- `reuse lint` is a required status check; `deny.toml` constrains dependency licenses to AGPL-compatible ones.
- Recipients may remove the additional permission from their copies (AGPL §7); the AGPL itself is unchanged for everyone.
- Store listings use "Seerr" descriptively only and none of Seerr's marks or artwork.
