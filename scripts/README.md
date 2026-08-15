History rewrite helper

This folder contains a PowerShell helper `remove_from_history.ps1` that
documents safe steps to remove large or generated files from git history.

Read and follow these guidelines carefully:

- Make a local backup of your repositories before attempting history rewrite.
- Use a mirror clone (git clone --mirror) for safe rewriting.
- Prefer `git-filter-repo` if available; BFG is simpler for many cases.
- Do NOT force-push until you have coordinated with all collaborators.

See `remove_from_history.ps1` for the example commands.
