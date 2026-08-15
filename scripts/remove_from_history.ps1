<#
Purpose: Prepare commands to remove large/generated files from git history

IMPORTANT: This script does NOT run any network or force-push commands.
It prints the recommended sequence and optionally runs local steps on a
mirror clone if you uncomment the 'Invoke-Expression' lines below.

Read the README in this folder before running. Back up your data and inform
all collaborators before performing a history rewrite.
#>

param(
    [string]$RepoUrl = '',
    [string]$MirrorDir = 'repo-mirror.git',
    [string]$BfgJar = 'bfg.jar'
)

Write-Host "=== Git History Cleanup Helper ===" -ForegroundColor Cyan

Write-Host "This script helps you remove generated artifacts (e.g. *.class, build/, target/, node_modules/) from your git history." -ForegroundColor Yellow

Write-Host "\nSTEP 0 — READ THIS:"
Write-Host " - This will rewrite git history. Coordinate with your team."
Write-Host " - You must force-push after the rewrite. I WILL NOT push for you."
Write-Host " - Recommended: use git-filter-repo (faster) or BFG (simpler)." -ForegroundColor Yellow

Write-Host "\nIf you already have a local clone with work, do NOT run the destructive push from that clone. Create a mirror clone as shown below." -ForegroundColor Magenta

Write-Host "\n---- Mirror clone your remote repository (example):" -ForegroundColor Green
Write-Host "git clone --mirror <REPO_URL> $MirrorDir"

Write-Host "\n---- Option A: Use BFG (simple)" -ForegroundColor Green
Write-Host "1) Download BFG (https://rtyley.github.io/bfg-repo-cleaner/) and put bfg.jar next to this script or in PATH."
Write-Host "2) Run (from parent dir of the mirror clone):"
Write-Host "   java -jar $BfgJar --delete-files '*.class' --delete-folders 'build' --delete-folders 'target' --delete-folders 'node_modules' --strip-blobs-bigger-than 10M $MirrorDir"
Write-Host "3) Prune and gc the repo metadata and verify:"
Write-Host "   cd $MirrorDir"
Write-Host "   git reflog expire --expire=now --all"
Write-Host "   git gc --prune=now --aggressive"
Write-Host "4) Inspect the mirror (optional): git log --stat --all"
Write-Host "5) When satisfied, force-push the cleaned refs to remote (this is destructive):"
Write-Host "   git push --force --all origin"
Write-Host "   git push --force --tags origin"

Write-Host "\n---- Option B: Use git-filter-repo (recommended)" -ForegroundColor Green
Write-Host "1) Install git-filter-repo (https://github.com/newren/git-filter-repo)."
Write-Host "2) Run (from inside the mirror clone):"
Write-Host "   git filter-repo --invert-paths --paths-glob '*.class' --paths-glob 'build/**' --paths-glob 'target/**' --paths-glob 'node_modules/**'"
Write-Host "3) Prune and gc, then inspect as above."
Write-Host "4) Force-push cleaned refs: git push --force --all origin && git push --force --tags origin"

Write-Host "\n---- Safety / Verification" -ForegroundColor Yellow
Write-Host " - Create a backup: copy the mirror directory somewhere safe before pushing."
Write-Host " - Verify history locally (git log, git rev-list --objects --all | sort -k2)"
Write-Host " - Announce to collaborators to re-clone or run: git fetch --all && git reset --hard origin/main"

Write-Host "\n---- Example full sequence (BFG) to run manually:" -ForegroundColor Cyan
Write-Host "git clone --mirror <REPO_URL> repo-mirror.git"
Write-Host "java -jar bfg.jar --delete-files '*.class' --delete-folders 'build' --delete-folders 'target' --delete-folders 'node_modules' --strip-blobs-bigger-than 10M repo-mirror.git"
Write-Host "cd repo-mirror.git"
Write-Host "git reflog expire --expire=now --all"
Write-Host "git gc --prune=now --aggressive"
Write-Host "git push --force --all origin"
Write-Host "git push --force --tags origin"

Write-Host "\nScript prepared at scripts/remove_from_history.ps1. I will not run the destructive push for you." -ForegroundColor Green

exit 0
