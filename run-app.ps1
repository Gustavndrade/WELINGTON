$ErrorActionPreference = "Stop"

$mavenVersion = "3.9.9"
$mavenZip = "apache-maven-$mavenVersion-bin.zip"
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/$mavenZip"
$mavenDir = ".maven"
$mavenHome = "$pwd\$mavenDir\apache-maven-$mavenVersion"
$mvnCmd = "$mavenHome\bin\mvn.cmd"

if (-not (Test-Path $mvnCmd)) {
    Write-Host "Maven não encontrado. Baixando Maven $mavenVersion..." -ForegroundColor Cyan
    if (-not (Test-Path $mavenDir)) {
        New-Item -ItemType Directory -Path $mavenDir | Out-Null
    }
    
    $zipPath = "$mavenDir\$mavenZip"
    Invoke-WebRequest -Uri $mavenUrl -OutFile $zipPath
    
    Write-Host "Extraindo Maven..." -ForegroundColor Cyan
    Expand-Archive -Path $zipPath -DestinationPath $mavenDir -Force
    Remove-Item -Path $zipPath
}

Write-Host "Iniciando a aplicação Spring Boot com o banco local PostgreSQL..." -ForegroundColor Green
Write-Host "Certifique-se de que o PostgreSQL está rodando, o banco 'aluguel_carros' está criado e a senha está correta." -ForegroundColor Yellow
Write-Host "--------------------------------------------------------" -ForegroundColor Cyan

$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:MAVEN_OPTS = "-Djavax.net.ssl.trustStoreType=WINDOWS-ROOT -Djavax.net.ssl.trustStore=NUL -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true"
& $mvnCmd spring-boot:run
