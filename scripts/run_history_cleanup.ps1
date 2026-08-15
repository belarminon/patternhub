<#
run_history_cleanup.ps1

Safe helper to perform local history cleanup steps up to, but not including,
the destructive force-push. The script will create a mirror clone and run
either BFG or git-filter-repo to remove patterns, then run maintenance (reflog/gc).

USAGE (example):
  .\run_history_cleanup.ps1 -RepoUrl 'https://github.com/you/repo.git' -Tool bfg -Confirm

Notes:
- This script WILL NOT run 'git push --force'. It prints the exact push commands
  for you to run manually after verifying the mirror.
- By default the script refuses to run unless -Confirm is passed.
- You must have Java (for BFG) or git-filter-repo installed for the selected tool.
#>

param(
    [Parameter(Mandatory=$true)]
    [string]$RepoUrl,

    [string]$MirrorDir = 'repo-mirror.git',

    [ValidateSet('bfg','filter-repo')]
    [string]$Tool = 'filter-repo',

    [string]$BfgJar = 'bfg.jar',

    [switch]$Confirm
)

function ExitWith($code, $msg) {
    Write-Host $msg -ForegroundColor Red
    exit $code
}

if (-not $Confirm) {
    Write-Host "Safety: this script will modify a local mirror clone but will NOT push to remote." -ForegroundColor Yellow
    Write-Host "Rerun with -Confirm to actually perform the local cleanup steps." -ForegroundColor Yellow
    Write-Host "Example: .\run_history_cleanup.ps1 -RepoUrl 'https://github.com/you/repo.git' -Tool filter-repo -Confirm"
    exit 0
}

if (Test-Path $MirrorDir) {
    ExitWith 2 "Target mirror directory '$MirrorDir' already exists. Remove or choose another MirrorDir and retry."
}

Write-Host "Cloning mirror: $RepoUrl -> $MirrorDir" -ForegroundColor Cyan
git clone --mirror $RepoUrl $MirrorDir
if ($LASTEXITCODE -ne 0) { ExitWith 3 "git clone --mirror failed." }

Push-Location $MirrorDir

try {
    if ($Tool -eq 'bfg') {
        if (-not (Get-Command java -ErrorAction SilentlyContinue)) { ExitWith 4 "Java not found in PATH; required for BFG." }
        if (-not (Test-Path "..\$BfgJar")) { Write-Host "Warning: $BfgJar not found next to script; ensure correct path or download BFG." -ForegroundColor Yellow }

        $bfgCmd = "java -jar `"..\$BfgJar`" --delete-files '*.class' --delete-folders 'build' --delete-folders 'target' --delete-folders 'node_modules' --strip-blobs-bigger-than 10M ."
        Write-Host "Running BFG (this rewrites the mirror only):`n$bfgCmd" -ForegroundColor Green
        iex $bfgCmd
        if ($LASTEXITCODE -ne 0) { ExitWith 5 "BFG run failed." }
    }
    else {
        # git-filter-repo path-glob examples
        if (-not (Get-Command git-filter-repo -ErrorAction SilentlyContinue)) {
            if (-not (Get-Command git filter-repo -ErrorAction SilentlyContinue)) {
                Write-Host "Warning: git-filter-repo command not found. Please install git-filter-repo." -ForegroundColor Yellow
                ExitWith 6 "git-filter-repo not available." 
            }
        }

        $filterCmd = "git filter-repo --invert-paths --paths-glob '*.class' --paths-glob 'build/**' --paths-glob 'target/**' --paths-glob 'node_modules/**'"
        Write-Host "Running git-filter-repo (this rewrites the mirror only):`n$filterCmd" -ForegroundColor Green
        iex $filterCmd
        if ($LASTEXITCODE -ne 0) { ExitWith 7 "git-filter-repo run failed." }
    }

    Write-Host "Pruning refs and running git gc..." -ForegroundColor Cyan
    git reflog expire --expire=now --all
    git gc --prune=now --aggressive
    Write-Host "Local mirror cleanup complete. Inspect the mirror before pushing." -ForegroundColor Green

    Write-Host "\nIMPORTANT: To push cleaned refs to origin (DESTRUCTIVE), run manually from the mirror directory:" -ForegroundColor Yellow
    Write-Host "git push --force --all origin" -ForegroundColor Magenta
    Write-Host "git push --force --tags origin" -ForegroundColor Magenta

    Write-Host "\nVerify history locally (examples):" -ForegroundColor Cyan
    Write-Host "git log --all --pretty=format:'%h %ad %s' --date=short | head -n 50"
    Write-Host "git rev-list --objects --all | sort -k2" -ForegroundColor Cyan

} finally {
    Pop-Location
}

Write-Host "Script finished (mirror created at $MirrorDir). No force-push was executed." -ForegroundColor Green

exit 0
