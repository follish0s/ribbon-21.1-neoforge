Add-Type -AssemblyName System.IO.Compression.FileSystem
$jar = "D:\ribbon-21.1-neoforge\.gradle\configuration\neoForm\neoFormJoined1.21.1-20240808.144430\raw.jar"
$zip = [System.IO.Compression.ZipFile]::OpenRead($jar)

Write-Output "### CANDIDATE CLASS NAMES ###"
$zip.Entries | Where-Object {
    $_.FullName -match "client/resources/model/(MultiPart|BlockModel|ModelManager|MultiPartBaked)" -or
    $_.FullName -match "renderer/block/model/multipart/" -or
    $_.FullName -match "client/renderer/block/BlockModelShaper"
} | ForEach-Object { Write-Output $_.FullName }

Write-Output ""
Write-Output "### CHUNK / NBT CANDIDATES ###"
$zip.Entries | Where-Object {
    $_.FullName -match "world/level/chunk/(LevelChunk|ProtoChunk|ChunkSerializer|LevelChunkSection)\.class"
} | ForEach-Object { Write-Output $_.FullName }

Write-Output ""
Write-Output "### FIELD DUMP ###"
$targets = @(
    "net/minecraft/world/level/block/state/BlockBehaviour`$BlockStateBase`$Cache.class",
    "net/minecraft/client/resources/model/MultiPartModel.class",
    "net/minecraft/client/renderer/block/model/multipart/Selector.class",
    "net/minecraft/client/renderer/block/model/multipart/MultiPartStateCondition.class",
    "net/minecraft/world/level/chunk/LevelChunk.class"
)
foreach ($t in $targets) {
    $entry = $zip.Entries | Where-Object { $_.FullName -eq $t }
    if ($null -eq $entry) { Write-Output "=== NOT FOUND: $t ==="; continue }
    $ms = New-Object System.IO.MemoryStream
    $s = $entry.Open(); $s.CopyTo($ms); $s.Close()
    $text = [System.Text.Encoding]::ASCII.GetString($ms.ToArray())
    Write-Output "=== $t ==="
    Write-Output (([regex]::Matches($text, '[a-zA-Z_][a-zA-Z0-9_$]{1,45}') | ForEach-Object { $_.Value } | Select-Object -Unique) -join " ")
    Write-Output ""
}
$zip.Dispose()
