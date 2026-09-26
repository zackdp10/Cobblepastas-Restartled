"""Package the checked-in Fabric mod contents; does not compile Java."""
from pathlib import Path
from zipfile import ZIP_DEFLATED, ZipFile

root = Path(__file__).resolve().parents[1]
output = root / 'dist' / 'Cobblepastas-v2.0.0-TEST.jar'
entries = [root / name for name in ('LICENSE_cobblepastas', 'README.txt', 'fabric.mod.json', 'pack.mcmeta', 'pack.png')]
for folder in ('META-INF', 'assets', 'data', 'com'):
    entries.extend(p for p in (root / folder).rglob('*') if p.is_file())
output.parent.mkdir(exist_ok=True)
with ZipFile(output, 'w', ZIP_DEFLATED) as archive:
    for path in sorted(entries, key=lambda p: p.relative_to(root).as_posix()):
        archive.write(path, path.relative_to(root).as_posix())
with ZipFile(output) as archive:
    assert archive.testzip() is None
print(output)
